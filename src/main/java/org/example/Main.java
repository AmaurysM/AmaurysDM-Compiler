package org.example;

import java.io.FileReader;
import java.io.PushbackReader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {

        String program = "var w\n" +
                "var x\n" +
                "var y\n" +
                "initialize w = 5\n" +
                "initialize x = 10\n" +
                "initialize y = 15\n" +
                "compute w = x + y + 4\n" +
                "if x = y then\n" +
                "output w\n" +
                "output x\n" +
                "endif";

        Parser parser = new Parser();
        boolean success = parser.parse(program);

        if (success) {
            System.out.println("\n--- Abstract Syntax Tree ---");
            AbsSynTree ast = parser.getAbsSynTree();
            ast.show();
        }

//        String program = "var x var y initialize x = 5 compute y = x + 10 output y";
//
//        Parser parser = new Parser();
//        boolean success = parser.parse(program);
//
//        if (success) {
//            System.out.println("Program parse successfully!");
//        } else {
//            System.out.println("Program failed to parse.");
//        }

//        try {
//            FileReader fr = new FileReader("input.txt");
//            PushbackReader pbr = new PushbackReader(fr);
//            Scanner scanner = new Scanner(pbr);
//
//            Scanner.TOKEN nextToken = scanner.scan();
//            while(nextToken != Scanner.TOKEN.EOF){
//                System.out.println(nextToken + " " + scanner.getBuffer());
//                nextToken = scanner.scan();
//            }
//        } catch (Exception ignore){
//            ;
//        }
    }
}