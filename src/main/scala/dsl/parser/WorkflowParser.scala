package dsl.parser

import dsl.token.{IDENTIFIER, WorkflowToken}

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

}

class WorkflowTokenReader(tokens: Seq[WorkflowToken]) extends Reader[WorkflowToken] {

  override def first: WorkflowToken = tokens.head

  override def rest: Reader[WorkflowToken] = new WorkflowTokenReader(tokens.tail)

  override def pos: Position = NoPosition

  override def atEnd: Boolean = tokens.isEmpty
}
