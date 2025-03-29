package org.example;

import java.io.FileReader;
import java.io.PushbackReader;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        try {
            FileReader fr = new FileReader("input.txt");
            PushbackReader pbr = new PushbackReader(fr);
            Scanner scanner = new Scanner(pbr);

            Scanner.TOKEN nextToken = scanner.scan();
            while(nextToken != Scanner.TOKEN.EOF){
                System.out.println(nextToken + " " + scanner.getBuffer());
                nextToken = scanner.scan();
            }
        } catch (Exception ignore){
            ;
        }
    }
}