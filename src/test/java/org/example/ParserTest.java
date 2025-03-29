package org.example;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
public class ParserTest {

    @Test
    void TestASuccess(){
        Parser parser = new Parser();
        String program = "var w\n" +
                "var x\n" +
                "var y\n" +
                "var z\n" +
                "initialize w = 5\n" +
                "initialize x = 10\n" +
                "initialize y = 15\n" +
                "initialize z = 20\n" +
                "compute w = x + y + z\n" +
                "if x = y then\n" +
                " output w\n" +
                " output x\n" +
                "endif\n" +
                "output y\n" +
                "output z";
        boolean success = parser.parse(program);
        assertTrue(success);
    }

    @Test
    void TestFail(){

        Parser parser = new Parser();
        String program = "var w\n" +
                "var x\n" +
                "initialize w = 5\n" +
                "initialize x = 10\n" +
                "output w\n" +
                "output x\n" +
                "if w x then\n" +
                " output w\n" +
                "endif";
        boolean success = parser.parse(program);
        assertFalse(success);

    }
//    @Test
//    void TestSuccess(){
//        Parser parser = new Parser();
//        boolean success = false;
//        try {
//            success = parser.parse(new FileReader("program1.txt"));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        assertTrue(success);
//
//    }
//
//    @Test
//    void TestFail(){
//
//        Parser parser = new Parser();
//        boolean success = false;
//        try {
//            success = parser.parse(new FileReader("program2.txt"));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        assertFalse(success);
//
//    }
}
