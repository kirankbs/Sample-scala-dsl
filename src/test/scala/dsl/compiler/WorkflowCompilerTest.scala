package dsl.compiler


import org.junit._
import org.scalatest.Matchers._

/**
  * Created by kirankumarbs on 12/12/2016.
  */
class WorkflowCompilerTest {

  @Test
  def itShouldGiveParsedOutput(): Unit ={
    //given
    val code = "read input name, country\nswitch:\n  country == \"PT\" ->\n    call service \"A\"\n    exit\n  otherwise ->\n    call service \"B\"\n    switch:\n      name == \"unknown\" ->\n        exit\n      otherwise ->\n        call service \"C\"\n        exit"
    val expected = "AndThen(\n  ReadInput(List(name, country)),\n  Choice(List(\n    IfThen(\n      Equals(country, PT),\n      AndThen(CallService(A), Exit)\n    ),\n    OtherwiseThen(\n      AndThen(\n        CallService(B),\n        Choice(List(\n          IfThen(Equals(name, unknown), Exit),\n          OtherwiseThen(AndThen(CallService(C), Exit))\n        ))\n      )\n    )\n  ))\n)"
    //when
    val actual = WorkflowCompiler(code)
    //then
    println(actual)
  }

}
