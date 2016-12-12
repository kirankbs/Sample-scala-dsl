package dsl.compiler

import dsl.lexer.WorkflowLexer
import dsl.parser.{WorkflowAST, WorkflowParser}

/**
  * Created by kirankumarbs on 12/12/2016.
  */
object WorkflowCompiler {
  def apply(code: String): Either[WorkflowCompilationError, WorkflowAST] = {
    for {
      tokens <- WorkflowLexer(code).right
      ast <- WorkflowParser(tokens).right
    } yield ast
  }
}
