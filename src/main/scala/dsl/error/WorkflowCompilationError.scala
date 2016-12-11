package dsl.error

/**
  * Created by kirankumarbs on 12/12/16.
  */
trait WorkflowCompilationError

case class WorkflowLexerError(msg: String) extends WorkflowCompilationError