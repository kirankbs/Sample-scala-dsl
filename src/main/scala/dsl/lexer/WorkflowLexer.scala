package dsl.lexer

import dsl.error.WorkflowLexerError
import dsl.token._

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

/**
  * Created by kirankumarbs on 12/12/16.
  */
object WorkflowLexer extends RegexParsers{

  override def skipWhitespace: Boolean = true

  override val whiteSpace: Regex = "[ \t\r\f]+".r


  def identifier: Parser[IDENTIFIER] = {
    "[a-zA-Z_][a-zA-Z0-9_]*".r ^^ (str => IDENTIFIER(str))
  }

  def literal: Parser[LITERAL] = {
    """"[^"]*"""".r ^^ (str => LITERAL(str))
  }

  def indentation: Parser[INDENTATION] = {
    "\n[ ]*".r ^^ { sp =>
      val nSpaces = sp.length - 1
      INDENTATION(nSpaces)
    }
  }

  def exit = "exit" ^^ (_ => EXIT)
  def readInput = "read input" ^^ (_ => READINPUT)
  def callService = " call service" ^^ (_ => CALLSERVICE)
  def switch = "switch" ^^ (_ => SWITCH)
  def otherwise = "otherwise" ^^ (_ => OTHERWISE)
  def colon = ":" ^^ (_ => COLON)
  def arrow = "->" ^^ (_ => ARROW)
  def equals = "==" ^^ (_ => EQUALS)
  def comma = "," ^^ (_ => COMMA)

  def tokens: Parser[List[WorkflowToken]] = {
    phrase(rep1(exit | readInput | callService | switch | otherwise | colon | arrow
      | equals | comma | literal | identifier | indentation)) ^^ { rawTokens =>
      processIndentations(rawTokens)
    }
  }

  private def processIndentations(tokens: List[WorkflowToken],
                                  indents: List[Int] = List(0)): List[WorkflowToken] = {
    tokens.headOption match {

      // if there is an increase in indentation level, we push this new level into the stack
      // and produce an INDENT
      case Some(INDENTATION(spaces)) if spaces > indents.head =>
        INDENT :: processIndentations(tokens.tail, spaces :: indents)

      // if there is a decrease, we pop from the stack until we have matched the new level,
      // producing a DEDENT for each pop
      case Some(INDENTATION(spaces)) if spaces < indents.head =>
        val (dropped, kept) = indents.partition(_ > spaces)
        (dropped map (_ => DEDENT)) ::: processIndentations(tokens.tail, kept)

      // if the indentation level stays unchanged, no tokens are produced
      case Some(INDENTATION(spaces)) if spaces == indents.head =>
        processIndentations(tokens.tail, indents)

      // other tokens are ignored
      case Some(token) =>
        token :: processIndentations(tokens.tail, indents)

      // the final step is to produce a DEDENT for each indentation level still remaining, thus
      // "closing" the remaining open INDENTS
      case None =>
        indents.filter(_ > 0).map(_ => DEDENT)

    }
  }

  def apply(code: String): Either[WorkflowLexerError, List[WorkflowToken]] = {
    parse(tokens, code) match {
      case NoSuccess(msg, next) => Left(WorkflowLexerError(msg))
      case Success(result, next) => Right(result)
    }
  }


}