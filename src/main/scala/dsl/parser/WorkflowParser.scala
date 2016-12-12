package dsl.parser

import dsl.compiler.{Location, WorkflowParserError}
import dsl.token._

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

/**
  * Created by kirankumarbs on 12/12/16.
  */
object WorkflowParser extends Parsers{

  override type Elem = WorkflowToken

  private def identifier: Parser[IDENTIFIER] = positioned {
    accept("identifier", { case id @ IDENTIFIER(name) => id})
  }

  private def literal: Parser[LITERAL] = positioned {
    accept("string literal", { case lit: LITERAL => lit})
  }

  def condition: Parser[Equals] = positioned {
    (identifier ~ EQUALS() ~ literal) ^^ { case id ~ eq ~ lit => Equals(id.str, lit.str) }
  }


  def program: Parser[WorkflowAST] = positioned {
    phrase(block)
  }

  def block: Parser[WorkflowAST] = positioned {
    rep1(statement) ^^ { case stmtList => stmtList reduceRight AndThen }
  }

  def statement: Parser[WorkflowAST] = positioned {
    val exit = EXIT() ^^ (_ => Exit)
    val readInput = READINPUT() ~ rep(identifier ~ COMMA()) ~ identifier ^^ {
      case read ~ inputs ~ IDENTIFIER(lastInput) => ReadInput(inputs.map(_._1.str) ++ List(lastInput))
    }
    val callService = CALLSERVICE() ~ literal ^^ {
      case call ~ LITERAL(serviceName) => CallService(serviceName)
    }
    val switch = SWITCH() ~ COLON() ~ INDENT() ~ rep1(ifThen) ~ opt(otherwiseThen) ~ DEDENT() ^^ {
      case _ ~ _ ~ _ ~ ifs ~ otherwise ~ _ => Choice(ifs ++ otherwise)
    }
    exit | readInput | callService | switch
  }

  def ifThen: Parser[IfThen] = positioned {
    (condition ~ ARROW() ~ INDENT() ~ block ~ DEDENT()) ^^ {
      case cond ~ _ ~ _ ~ block ~ _ => IfThen(cond, block)
    }
  }

  def otherwiseThen: Parser[OtherwiseThen] = positioned {
    (OTHERWISE() ~ ARROW() ~ INDENT() ~ block ~ DEDENT()) ^^ {
      case _ ~ _ ~ _ ~ block ~ _ => OtherwiseThen(block)
    }
  }

  def apply(tokens: Seq[WorkflowToken]): Either[WorkflowParserError, WorkflowAST] = {
    val reader = new WorkflowTokenReader(tokens)
    program(reader) match {
      case NoSuccess(msg, next) => Left(WorkflowParserError(Location(next.pos.line,next.pos.column),msg))
      case Success(result, next) => Right(result)
    }
  }

}

class WorkflowTokenReader(tokens: Seq[WorkflowToken]) extends Reader[WorkflowToken] {

  override def first: WorkflowToken = tokens.head

  override def rest: Reader[WorkflowToken] = new WorkflowTokenReader(tokens.tail)

  override def pos: Position = tokens.headOption.map(_.pos).getOrElse(NoPosition)

  override def atEnd: Boolean = tokens.isEmpty
}
