package org.example;

import java.io.FileReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

public class Parser {

    enum TYPE {
        INTDATATYPE
    }

    private HashMap<String,SymbolTableItem>symbolTable = new HashMap<>();

    private Scanner scanner;
    private Scanner.TOKEN nextToken;


    public void match(Scanner.TOKEN expected) throws Exception {
        if (nextToken == expected){
            System.out.println("\nThe token was matched. \n~~{ Buffer:- " + nextToken + ";"+ expected + " -:Token }~~\n");
            nextToken = scanner.scan();
        }else {
            throw new Exception(
                    "Parse " +
                    "\nExpected: " + expected +
                    "\nFound token: " + nextToken +
                    "\nWith lexeme: " + scanner.getBuffer()
            );
        }
    }

//    /**
//     * Parses the input from a given {@link Reader}. (File reader, string reader...)
//     * <p>
//     * This method creates a {@link PushbackReader} from the give reader, tokenizes the input,
//     * and tries to parse it.
//     * <p>
//     * <b>
//     * The assignment asked for a string input but in class we used a file reader.
//     * I split the difference and used both.
//     * <p>
//     *
//     * @param reader the {@link Reader} from which to read the input.
//     * @return {@code true} if parsing completed successfully; {@code false} if not.
//     */
//    public boolean parse(Reader reader) {
//        try {
//            PushbackReader pbr = new PushbackReader(reader);
//            scanner = new Scanner(pbr);
//            nextToken = scanner.scan();
//            program();
//
//            if (nextToken != Scanner.TOKEN.EOF) {
//                System.err.println(
//                        "Expected end of file" +
//                                "\nFound token: " + nextToken +
//                                "\nlexeme: " + scanner.getBuffer()
//                );
//                return false;
//            }
//            System.out.println("/\\/\\/\\/\\/\\/\\/\\/\\{ Parsing Completed }/\\/\\/\\/\\/\\/\\/\\/\\");
//            return true;
//        } catch (Exception e) {
//            System.err.println("Parsing failed: " + e.getMessage());
//            return false;
//        }
//    }

//    public boolean parse(FileReader file) {
//        try {
//            PushbackReader pbr = new PushbackReader(file);
//            scanner = new Scanner(pbr);
//            nextToken = scanner.scan();
//            program();
//
//            if (nextToken != Scanner.TOKEN.EOF) {
//                System.err.println(
//                        "Expected end of file" +
//                        "\nFound token: " + nextToken +
//                        "\nlexeme: " + scanner.getBuffer()
//                );
//                return false;
//            }
//            System.out.println("/\\/\\/\\/\\/\\/\\/\\/\\{ Parsing Completed }/\\/\\/\\/\\/\\/\\/\\/\\");
//            return true;
//        } catch (Exception e) {
//            System.err.println("Parsing failed: " + e.getMessage());
//            return false;
//        }
//    }
//
    public boolean parse(String program) {
        try {
            scanner = new Scanner(new PushbackReader(new StringReader(program)));
            nextToken = scanner.scan();
            program();

            if (nextToken != Scanner.TOKEN.EOF) {
                System.err.println(
                        "Expected end of file" +
                        "\nFound token: " + nextToken +
                        "\nlexeme: " + scanner.getBuffer()
                );
                return false;
            }
            System.out.println("/\\/\\/\\/\\/\\/\\/\\/\\{ Parsing Completed }/\\/\\/\\/\\/\\/\\/\\/\\");
            return true;
        } catch (Exception e) {
            System.err.println("Parsing failed: " + e.getMessage());
            return false;
        }
    }

    private void program() throws Exception {
        vars();
        stmts();
    }


    private void vars() throws Exception {
        while (nextToken == Scanner.TOKEN.VAR) {
            varDecl();
        }
    }


    private void varDecl() throws Exception {
        match(Scanner.TOKEN.VAR);
        if (nextToken == Scanner.TOKEN.ID) {
            String varName = scanner.getBuffer();
            if (symbolTable.containsKey(varName)) {
                throw new Exception("Variable with name '" + varName + "' has already been declared.");
            }
            symbolTable.put(varName, new SymbolTableItem(varName, TYPE.INTDATATYPE));
            System.out.println("Declared variable: '" + varName + "'");
            match(Scanner.TOKEN.ID);
        } else {
            throw new Exception(
                    "Expected an identifier after 'var' \n"
                    + "Token: " + nextToken
                    + "\nlexeme: " + scanner.getBuffer()
            );
        }
    }


    private void stmts() throws Exception {
        while (nextToken == Scanner.TOKEN.OUTPUT ||
                nextToken == Scanner.TOKEN.INITIALIZE ||
                nextToken == Scanner.TOKEN.IF ||
                nextToken == Scanner.TOKEN.COMPUTE) {
            stmt();
        }
    }


    private void stmt() throws Exception {
        switch (nextToken) {
            case OUTPUT:
                match(Scanner.TOKEN.OUTPUT);
                match(Scanner.TOKEN.ID);
                break;
            case INITIALIZE:
                match(Scanner.TOKEN.INITIALIZE);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                match(Scanner.TOKEN.CONSTINT);
                break;
            case IF:
                match(Scanner.TOKEN.IF);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.THEN);
                stmts();
                match(Scanner.TOKEN.ENDIF);
                break;
            case COMPUTE:
                match(Scanner.TOKEN.COMPUTE);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                add();
                break;
            default:
                throw new Exception("stmt"+ "\nUnexpected token in statement: " + nextToken +
                        "\nlexeme: " + scanner.getBuffer());
        }
    }


    private void add() throws Exception {
        value();
        addEnd();
    }

    private void value() throws Exception {
        if (nextToken == Scanner.TOKEN.ID) {
            match(Scanner.TOKEN.ID);
        } else if (nextToken == Scanner.TOKEN.CONSTINT) {
            match(Scanner.TOKEN.CONSTINT);
        } else {
            throw new Exception("Expected a value (id or constint) "
                    + "\nfound token: " + nextToken
                    + "\n lexeme: " + scanner.getBuffer());
        }
    }

    private void addEnd() throws Exception {
        while (nextToken == Scanner.TOKEN.PLUS) {
            match(Scanner.TOKEN.PLUS);
            value();
        }
    }
}
