package dsl.compiler

/**
  * Created by kirankumarbs on 12/12/16.
  */
trait WorkflowCompilationError

case class WorkflowLexerError(location: Location,msg: String) extends WorkflowCompilationError

case class WorkflowParserError(location: Location,msg: String) extends WorkflowCompilationError


case class Location(line: Int, column: Int){
  override def toString = s"$line:$column"
}