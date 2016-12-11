package dsl.parser

import dsl.token._

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

/**
  * Created by kirankumarbs on 12/12/16.
  */
object WorkflowParser extends Parsers{

  override type Elem = WorkflowToken

  private def identifier: Parser[IDENTIFIER] = {
    accept("identifier", { case id @ IDENTIFIER(name) => id})
  }

  private def literal: Parser[LITERAL] = {
    accept("string literal", { case lit @ LITERAL(name) => lit})
  }

  def condition: Parser[Equals] = {
    (identifier ~ EQUALS ~ literal) ^^ { case id ~ eq ~ lit => Equals(id.str, lit.str) }
  }


  def program: Parser[WorkflowAST] = {
    phrase(block)
  }

  def block: Parser[WorkflowAST] = {
    rep1(statement) ^^ { case stmtList => stmtList reduceRight AndThen }
  }

  def statement: Parser[WorkflowAST] = {
    val exit = EXIT ^^ (_ => Exit)
    val readInput = READINPUT ~ rep(identifier ~ COMMA) ~ identifier ^^ {
      case read ~ inputs ~ IDENTIFIER(lastInput) => ReadInput(inputs.map(_._1.str) ++ List(lastInput))
    }
    val callService = CALLSERVICE ~ literal ^^ {
      case call ~ LITERAL(serviceName) => CallService(serviceName)
    }
    val switch = SWITCH ~ COLON ~ INDENT ~ rep1(ifThen) ~ opt(otherwiseThen) ~ DEDENT ^^ {
      case _ ~ _ ~ _ ~ ifs ~ otherwise ~ _ => Choice(ifs ++ otherwise)
    }
    exit | readInput | callService | switch
  }

  def ifThen: Parser[IfThen] = {
    (condition ~ ARROW ~ INDENT ~ block ~ DEDENT) ^^ {
      case cond ~ _ ~ _ ~ block ~ _ => IfThen(cond, block)
    }
  }

  def otherwiseThen: Parser[OtherwiseThen] = {
    (OTHERWISE ~ ARROW ~ INDENT ~ block ~ DEDENT) ^^ {
      case _ ~ _ ~ _ ~ block ~ _ => OtherwiseThen(block)
    }
  }

}

class WorkflowTokenReader(tokens: Seq[WorkflowToken]) extends Reader[WorkflowToken] {

  override def first: WorkflowToken = tokens.head

  override def rest: Reader[WorkflowToken] = new WorkflowTokenReader(tokens.tail)

  override def pos: Position = NoPosition

  override def atEnd: Boolean = tokens.isEmpty
}
