package org.example;

import program.PseudoAssemblyWithStringProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
//        String program = """
//                var a
//                var b
//                var c
//                var result
//                initialize a = 5
//                initialize b = 20
//                initialize c = 15
//                compute result = a + b
//                if a = b then
//                    output result
//                endif
//                compute result = a + c
//                if result = b then
//                    output result
//                endif
//                """;
        String program = readFile("program.txt");
        if (program == null) {
            System.exit(1);
        }
        
        
        Parser parser = new Parser();
        if (parser.parse(program)) {
            processParsedProgram(parser);
        } else {
            System.err.println("Parsing failed.");
            System.exit(1);
        }
    }
    
    private static String readFile(String filePath) {
        StringBuilder program = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                program.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + filePath);
            e.printStackTrace();
            return null;
        }
        return program.toString();
    }
    
    private static void processParsedProgram(Parser parser) {
        AbsSynTree ast = parser.getAbsSynTree();
        
        System.out.println("\n~~~~~~ Abstract Syntax Tree ~~~~~~");
        ast.show();
        
        String pseudoAsm = ast.getCode();
        System.out.println("\n~~~~~~ Pseudo‑Assembly ~~~~~~");
        System.out.println(pseudoAsm);
        
        int numVirtualRegistersInt = 32;
        int numVirtualRegistersString = 32;
        String outputClassName = "MyLabProgram";
        String outputPackage = "mypackage";
        String classesOutputDir = System.getProperty("user.dir") + "/target/classes";
        
        PseudoAssemblyWithStringProgram generator = new PseudoAssemblyWithStringProgram(
                pseudoAsm, outputClassName, outputPackage, classesOutputDir,
                numVirtualRegistersInt, numVirtualRegistersString
        );
        
        if (!generator.parse()) {
            System.err.println("Failed to parse pseudo‑assembly.");
            System.exit(2);
        }
        
        generator.generateBytecode();
        generator.run(new PrintStream(System.out));
    }
}
