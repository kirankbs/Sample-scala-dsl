package dsl.token

import org.junit.Test
import org.scalatest.Matchers._

/**
  * Created by kirankumarbs on 11/12/16.
  */
class TokenTest {

  @Test
  def itShouldCreateIDENTIFIERToken(): Unit ={
    //given
    //when
    val id = IDENTIFIER("20")
    //then
    id shouldBe an [IDENTIFIER]
    id.str should equal("20")
  }

  @Test
  def itShouldCreateLITERALToken(): Unit ={
    //given
    //when
    val literal = LITERAL("\"xyz\"")
    //then
    literal shouldBe an [LITERAL]
    literal.str should equal("\"xyz\"")
  }

  @Test
  def itShouldCreateINDENTATIONToken(): Unit ={
    //given
    //when
    val spaces = INDENTATION(2)
    //then
    spaces shouldBe an [INDENTATION]
    spaces.spaces should equal(2)

  }

  @Test
  def itShouldCreateEXITToken(): Unit ={
    //given
    //when
    val spaces = EXIT
    //then
    spaces shouldBe an [EXIT.type ]
  }

  @Test
  def itShouldCreateREADINPUTToken(): Unit ={
    //given
    //when
    val spaces = READINPUT
    //then
    spaces shouldBe an [READINPUT.type ]
  }

  @Test
  def itShouldCreateCALLSERVICEToken(): Unit ={
    //given
    //when
    val spaces = CALLSERVICE
    //then
    spaces shouldBe an [CALLSERVICE.type ]
  }

  @Test
  def itShouldCreateSWITCHToken(): Unit ={
    //given
    //when
    val spaces = SWITCH
    //then
    spaces shouldBe an [SWITCH.type ]
  }

  @Test
  def itShouldCreateOTHERWISEToken(): Unit ={
    //given
    //when
    val spaces = OTHERWISE
    //then
    spaces shouldBe an [OTHERWISE.type ]
  }

  @Test
  def itShouldCreateCOLONToken(): Unit ={
    //given
    //when
    val spaces = COLON
    //then
    spaces shouldBe an [COLON.type ]
  }

  @Test
  def itShouldCreateARROWToken(): Unit ={
    //given
    //when
    val spaces = ARROW
    //then
    spaces shouldBe an [ARROW.type ]
  }

  @Test
  def itShouldCreateEQUALSToken(): Unit ={
    //given
    //when
    val spaces = EQUALS
    //then
    spaces shouldBe an [EQUALS.type ]
  }

  @Test
  def itShouldCreateCOMMAToken(): Unit ={
    //given
    //when
    val spaces = COMMA
    //then
    spaces shouldBe an [COMMA.type ]
  }

  @Test
  def itShouldCreateINDENTToken(): Unit ={
    //given
    //when
    val spaces = INDENT
    //then
    spaces shouldBe an [INDENT.type ]
  }

  @Test
  def itShouldCreateDEDENTToken(): Unit ={
    //given
    //when
    val spaces = DEDENT
    //then
    spaces shouldBe an [DEDENT.type ]
  }



}
