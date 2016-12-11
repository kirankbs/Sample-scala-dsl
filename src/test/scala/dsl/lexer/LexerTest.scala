package dsl.lexer
import org.junit._
import org.scalatest.Matchers._

/**
  * Created by kirankumarbs on 12/12/16.
  */
class LexerTest {

  @Test
  def itShouldSkipWhiteSpaces(): Unit ={
    //given
    val expected = true
    //when
    val wLexer = WorkflowLexer.skipWhitespace
    //then
    wLexer should equal(expected)
  }

  @Test
  def itShouldGenerateListOfTokensBasedOnInput(): Unit ={
    //given
    val code = "read input name, country\nswitch:\n  country == \"PT\" ->\n    call service \"A\"\n    exit\n  otherwise ->\n    call service \"B\"\n    switch:\n      name == \"unknown\" ->\n        exit\n      otherwise ->\n        call service \"C\"\n        exit"
    //when
    val actual = WorkflowLexer(code)
    //then
    println(actual)
  }

  @Test
  def itShouldGenerateListOfTokensWhenInputStartsWithIdentifier(): Unit ={
    //given
    val code = "xyz read input name, country\nswitch:\n  country == \"PT\" ->\n    call service \"A\"\n    exit\n  otherwise ->\n    call service \"B\"\n    switch:\n      name == \"unknown\" ->\n        exit\n      otherwise ->\n        call service \"C\"\n        exit"
    //when
    val actual = WorkflowLexer(code)
    //then
    println(actual)
  }

}
