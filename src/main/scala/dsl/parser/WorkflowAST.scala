package dsl.parser

/**
  * Created by kirankumarbs on 12/12/16.
  */
sealed trait WorkflowAST
case class AndThen(step1: WorkflowAST, step2: WorkflowAST) extends WorkflowAST
case class ReadInput(inputs: Seq[String]) extends WorkflowAST
case class CallService(serviceName: String) extends WorkflowAST
case class choice(alternatives: Seq[ConditionThen]) extends WorkflowAST
case object EXIT extends WorkflowAST

sealed trait ConditionThen { def thenBlock: WorkflowAST }
case class IfThen(predicate: Condition, thenBlock: WorkflowAST) extends ConditionThen
case class OtherwiseThen(thenBlock: WorkflowAST) extends ConditionThen

sealed trait Condition
case class Equals(factName: String, factValue: String) extends Condition



