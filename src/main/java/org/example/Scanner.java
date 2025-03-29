package org.example;

import java.io.PushbackReader;
import java.util.Arrays;
import java.util.HashSet;

public class Scanner {

    public enum TOKEN{
        SCANOF, ID, CONSTINT, VAR, OUTPUT, INITIALIZE, EQUALS, IF, THEN, ENDIF, COMPUTE, PLUS, EOF
    }

    HashSet<String> reservedWords = new HashSet<>(
            Arrays.asList(
                    "var", "output", "initialize", "if", "then", "endif", "compute"
            )
    );

    private PushbackReader pbr;
    private final StringBuilder buffer = new StringBuilder();

    public Scanner(PushbackReader pbr) {this.pbr = pbr;}

    public TOKEN scan(){
        try {
            int c;
            c = pbr.read();

            while(c != -1) {

                buffer.setLength(0);

                if (Character.isWhitespace(c)){
                    c = pbr.read();

                } else {
                    while (!Character.isWhitespace(c) && c != -1) {
                        buffer.append((char)c);
                        c = pbr.read();
                    }

                    pbr.unread(c);

                    TOKEN foundToken = checkTokens(buffer);
                    if(foundToken != null) {
                        return foundToken;
                    }
                }
            }

        } catch (Exception ignore) {
            ;
        }

        return TOKEN.EOF;
    }

    public String getBuffer() {
        return buffer.toString();
    }

    private TOKEN checkTokens(StringBuilder buffer) {
        String lexeme = buffer.toString();

        if(lexeme.equalsIgnoreCase("SCANOF")){
            return TOKEN.SCANOF;
        }

        if(lexeme.matches("\\d+")){
            return TOKEN.CONSTINT;
        }

        if(lexeme.equals("+")){
            return TOKEN.PLUS;
        }

        if(lexeme.equals("=")){
            return TOKEN.EQUALS;
        }
        //System.out.println("reservedWord : " + reservedWords.contains(lexeme.toLowerCase()) +"->"+ lexeme.toLowerCase());

        if(reservedWords.contains(lexeme.toLowerCase())){
            //System.out.print("reserved Word: ");
            return TOKEN.valueOf(lexeme.toUpperCase());
        }

        if(lexeme.matches("[a-zA-Z].*")){
            return TOKEN.ID;
        }

        return null;
    }

}
