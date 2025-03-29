package org.example;

import java.io.FileReader;
import java.io.PushbackReader;
import java.io.StringReader;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        String program = "var x var y initialize x = 5 compute y = x + 10 output y";

        Parser parser = new Parser();
        boolean success = parser.parse(program);

        if (success) {
            System.out.println("Program parse successfully!");
        } else {
            System.out.println("Program failed to parse.");
        }

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