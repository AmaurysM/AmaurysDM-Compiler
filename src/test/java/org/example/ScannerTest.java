package org.example;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.example.Scanner.TOKEN;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PushbackReader;
import java.io.StringReader;


import static org.junit.jupiter.api.Assertions.*;


class ScannerTest {


    @Test
    void Test1(){
        StringReader sr = new StringReader("var score123");
        PushbackReader pbr = new PushbackReader( sr);
        Scanner scanner = new Scanner(pbr);


        assertEquals(TOKEN.VAR,scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("score123", scanner.getBuffer());
        assertEquals(TOKEN.EOF, scanner.scan());
    }


    @Test
    void Test2(){
        StringReader sr = new StringReader("initialize salary = 500");
        PushbackReader pbr = new PushbackReader( sr);
        Scanner scanner = new Scanner(pbr);


        assertEquals(TOKEN.INITIALIZE,scanner.scan());
        assertEquals("initialize", scanner.getBuffer());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("salary", scanner.getBuffer());
        assertEquals(TOKEN.EQUALS, scanner.scan());
        assertEquals(TOKEN.CONSTINT, scanner.scan());
        assertEquals(TOKEN.EOF, scanner.scan());
    }


    @Test
    void Test3(){
        StringReader sr = new StringReader("compute newsalary = originalsalary + raise");
        PushbackReader pbr = new PushbackReader( sr);
        Scanner scanner = new Scanner(pbr);


        assertEquals(TOKEN.COMPUTE,scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("newsalary", scanner.getBuffer());
        assertEquals(TOKEN.EQUALS, scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("originalsalary", scanner.getBuffer());
        assertEquals(TOKEN.PLUS, scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("raise", scanner.getBuffer());
        assertEquals(TOKEN.EOF, scanner.scan());
    }


    @Test
    void Test4(){
        StringReader sr = new StringReader("if x = y then endif");
        PushbackReader pbr = new PushbackReader( sr);
        Scanner scanner = new Scanner(pbr);


        assertEquals(TOKEN.IF,scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("x", scanner.getBuffer());
        assertEquals(TOKEN.EQUALS, scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("y", scanner.getBuffer());
        assertEquals(TOKEN.THEN, scanner.scan());
        assertEquals(TOKEN.ENDIF, scanner.scan());
        assertEquals(TOKEN.EOF, scanner.scan());
    }


    @Test
    void Test5(){
        StringReader sr = new StringReader("if x = y then output x endif");
        PushbackReader pbr = new PushbackReader( sr);
        Scanner scanner = new Scanner(pbr);


        assertEquals(TOKEN.IF,scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("x", scanner.getBuffer());
        assertEquals(TOKEN.EQUALS, scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("y", scanner.getBuffer());
        assertEquals(TOKEN.THEN, scanner.scan());
        assertEquals(TOKEN.OUTPUT, scanner.scan());
        assertEquals(TOKEN.ID,scanner.scan());
        assertEquals("x", scanner.getBuffer());
        assertEquals(TOKEN.ENDIF, scanner.scan());
        assertEquals(TOKEN.EOF, scanner.scan());
    }


    @Test
    void Test6(){
        StringReader sr = new StringReader("");
        PushbackReader pbr = new PushbackReader( sr);
        Scanner scanner = new Scanner(pbr);


        assertEquals(TOKEN.EOF,scanner.scan());
    }


}




