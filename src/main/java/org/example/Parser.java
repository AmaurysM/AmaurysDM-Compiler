package org.example;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;

public class Parser {

    enum TYPE {
        INTDATATYPE
    }

    private HashMap<String,SymbolTableItem> symbolTable = new HashMap<>();
    private Scanner scanner;
    private Scanner.TOKEN nextToken;
    private AbsSynTree absSynTree;

    public Parser() {
        this.absSynTree = new AbsSynTree();
    }

    public AbsSynTree getAbsSynTree() {
        return absSynTree;
    }

    public void match(Scanner.TOKEN expected) throws Exception {
        if (nextToken == expected){
            System.out.println("\nThe token was matched. \n~~{ Buffer:- " + nextToken + ";"+ expected + " -:Token }~~\n");
            nextToken = scanner.scan();
        } else {
            throw new Exception(
                    "Parse " +
                            "\nExpected: " + expected +
                            "\nFound token: " + nextToken +
                            "\nWith lexeme: " + scanner.getBuffer()
            );
        }
    }

    public boolean parse(String program) {
        try {
            scanner = new Scanner(new PushbackReader(new StringReader(program)));
            nextToken = scanner.scan();
            AbsSynTree.NodeProgram rootNode = program();
            absSynTree.setRoot(rootNode);

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

    private AbsSynTree.NodeProgram program() throws Exception {
        AbsSynTree.NodeVars varsNode = vars();
        AbsSynTree.NodeStmts stmtsNode = stmts();
        return absSynTree.new NodeProgram(varsNode, stmtsNode);
    }

    private AbsSynTree.NodeVars vars() throws Exception {
        AbsSynTree.NodeVars varsNode = absSynTree.new NodeVars();
        while (nextToken == Scanner.TOKEN.VAR) {
            AbsSynTree.NodeId varNode = varDecl();
            varsNode.addVar(varNode);
        }
        return varsNode;
    }

    private AbsSynTree.NodeId varDecl() throws Exception {
        match(Scanner.TOKEN.VAR);
        if (nextToken == Scanner.TOKEN.ID) {
            String varName = scanner.getBuffer();
            if (symbolTable.containsKey(varName)) {
                throw new Exception("Variable with name '" + varName + "' has already been declared.");
            }
            symbolTable.put(varName, new SymbolTableItem(varName, TYPE.INTDATATYPE));
            System.out.println("Declared variable: '" + varName + "'");
            AbsSynTree.NodeId idNode = absSynTree.new NodeId(varName);
            match(Scanner.TOKEN.ID);
            return idNode;
        } else {
            throw new Exception(
                    "Expected an identifier after 'var' \n"
                            + "Token: " + nextToken
                            + "\nlexeme: " + scanner.getBuffer()
            );
        }
    }

    private AbsSynTree.NodeStmts stmts() throws Exception {
        AbsSynTree.NodeStmts stmtsNode = absSynTree.new NodeStmts();
        while (nextToken == Scanner.TOKEN.OUTPUT ||
                nextToken == Scanner.TOKEN.INITIALIZE ||
                nextToken == Scanner.TOKEN.IF ||
                nextToken == Scanner.TOKEN.COMPUTE) {
            AbsSynTree.NodeStmt stmtNode = stmt();
            stmtsNode.addStmt(stmtNode);
        }
        return stmtsNode;
    }

    private AbsSynTree.NodeStmt stmt() throws Exception {
        switch (nextToken) {
            case OUTPUT:
                match(Scanner.TOKEN.OUTPUT);
                String outputVarName = scanner.getBuffer();
                if (!symbolTable.containsKey(outputVarName)) {
                    throw new Exception("Undeclared variable: " + outputVarName);
                }
                AbsSynTree.NodeId outputVar = absSynTree.new NodeId(outputVarName);
                match(Scanner.TOKEN.ID);
                return absSynTree.new NodeOutput(outputVar);

            case INITIALIZE:
                match(Scanner.TOKEN.INITIALIZE);
                String initVarName = scanner.getBuffer();
                if (!symbolTable.containsKey(initVarName)) {
                    throw new Exception("Undeclared variable: " + initVarName);
                }
                AbsSynTree.NodeId initVar = absSynTree.new NodeId(initVarName);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                String constValue = scanner.getBuffer();
                AbsSynTree.NodeConstInt constNode = absSynTree.new NodeConstInt(Integer.parseInt(constValue));
                match(Scanner.TOKEN.CONSTINT);
                return absSynTree.new NodeInitialize(initVar, constNode);

            case IF:
                match(Scanner.TOKEN.IF);
                String leftVarName = scanner.getBuffer();
                if (!symbolTable.containsKey(leftVarName)) {
                    throw new Exception("Undeclared variable: " + leftVarName);
                }
                AbsSynTree.NodeId leftVar = absSynTree.new NodeId(leftVarName);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                String rightVarName = scanner.getBuffer();
                if (!symbolTable.containsKey(rightVarName)) {
                    throw new Exception("Undeclared variable: " + rightVarName);
                }
                AbsSynTree.NodeId rightVar = absSynTree.new NodeId(rightVarName);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.THEN);
                AbsSynTree.NodeStmts ifStmts = stmts();
                match(Scanner.TOKEN.ENDIF);
                return absSynTree.new NodeIf(leftVar, rightVar, ifStmts);

            case COMPUTE:
                match(Scanner.TOKEN.COMPUTE);
                String computeVarName = scanner.getBuffer();
                if (!symbolTable.containsKey(computeVarName)) {
                    throw new Exception("Undeclared variable: " + computeVarName);
                }
                AbsSynTree.NodeId computeVar = absSynTree.new NodeId(computeVarName);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                AbsSynTree.NodeExpr expr = add();
                return absSynTree.new NodeCompute(computeVar, expr);

            default:
                throw new Exception("stmt"+ "\nUnexpected token in statement: " + nextToken +
                        "\nlexeme: " + scanner.getBuffer());
        }
    }

//    // This creates the correct output but its right to left
//    private AbsSynTree.NodeExpr add() throws Exception {
//        AbsSynTree.NodeExpr left = value();
//        if (nextToken == Scanner.TOKEN.PLUS) {
//            match(Scanner.TOKEN.PLUS);
//            AbsSynTree.NodeExpr right = add();
//            return absSynTree.new NodePlus(left, right);
//        } else {
//            return left;
//        }
//    }
    
    // This is left to right but the generated output is not the same as the one given to us.
    private AbsSynTree.NodeExpr add() throws Exception {
        AbsSynTree.NodeExpr leftExpr = value();
        return addEnd(leftExpr);
    }
    
    private AbsSynTree.NodeExpr value() throws Exception {
        if (nextToken == Scanner.TOKEN.ID) {
            String varName = scanner.getBuffer();
            if (!symbolTable.containsKey(varName)) {
                throw new Exception("Undeclared variable: " + varName);
            }
            AbsSynTree.NodeId idNode = absSynTree.new NodeId(varName);
            match(Scanner.TOKEN.ID);
            return idNode;
        } else if (nextToken == Scanner.TOKEN.CONSTINT) {
            String constValue = scanner.getBuffer();
            AbsSynTree.NodeConstInt constNode = absSynTree.new NodeConstInt(Integer.parseInt(constValue));
            match(Scanner.TOKEN.CONSTINT);
            return constNode;
        } else {
            throw new Exception("Expected a value (id or constint) "
                    + "\nfound token: " + nextToken
                    + "\n lexeme: " + scanner.getBuffer());
        }
    }

    private AbsSynTree.NodeExpr addEnd(AbsSynTree.NodeExpr leftExpr) throws Exception {
        while (nextToken == Scanner.TOKEN.PLUS) {
            match(Scanner.TOKEN.PLUS);
            AbsSynTree.NodeExpr rightExpr = value();
            leftExpr = absSynTree.new NodePlus(leftExpr, rightExpr);
        }
        return leftExpr;
    }

}