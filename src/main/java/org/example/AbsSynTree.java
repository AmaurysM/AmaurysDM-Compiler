package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsSynTree {
    private NodeProgram root;
    private List<String> codeLines = new ArrayList<>();
    private int intRegisterCounter = 1;
    private Map<String, String> varToRegister = new HashMap<>();
    private int labelCounter = 1;
    private String getNextLabel(String base) {
        return base + (labelCounter++);
    }
    
    public NodeProgram getRoot() {
        return root;
    }
    
    public void setRoot(NodeProgram root) {
        this.root = root;
    }
    
    public void show() {
        if (root != null) {
            root.show();
        } else {
            System.out.println("Empty abstract syntax tree");
        }
    }
    
    public String getCode() {
        codeLines.clear();
        intRegisterCounter = 1;
        varToRegister.clear();
        
        codeLines.add(".data");
        if (root != null) {
            root.createCode();
        }
        return String.join("\n", codeLines);
    }
    
    private String getNextIntRegister() {
        return "ri" + (intRegisterCounter++);
    }
    
    public abstract class NodeBase {
        public abstract void show();
        public abstract String createCode();
    }
    
    public abstract class NodeExpr extends NodeBase {}
    public abstract class NodeStmt extends NodeBase {}
    
    public class NodeId extends NodeExpr {
        private String name;
        public NodeId(String name) { this.name = name; }
        public String getName() { return name; }
        @Override
        public void show() { System.out.println("AST id " + name); }
        @Override
        public String createCode() {
            if (varToRegister.containsKey(name)) {
                return varToRegister.get(name);
            }
            String reg = getNextIntRegister();
            codeLines.add("loadintvar " + reg + ", " + name);
            varToRegister.put(name, reg);
            return reg;
        }
    }
    
    public class NodeConstInt extends NodeExpr {
        private int value;
        public NodeConstInt(int value) { this.value = value; }
        @Override
        public void show() { System.out.println("AST const int " + value); }
        @Override
        public String createCode() {
            String reg = getNextIntRegister();
            codeLines.add("loadintliteral " + reg + ", " + value);
            return reg;
        }
    }
    
    public class NodePlus extends NodeExpr {
        private NodeExpr left, right;
        public NodePlus(NodeExpr left, NodeExpr right) {
            this.left = left;
            this.right = right;
        }
        @Override
        public void show() {
            System.out.println("AST plus");
            System.out.print("LHS: "); left.show();
            System.out.print("RHS: "); right.show();
        }
        @Override
        public String createCode() {
            String leftReg = left.createCode();
            String rightReg = right.createCode();
            String resultReg = getNextIntRegister();
            codeLines.add("add " + leftReg + ", " + rightReg +", "+resultReg);
            return resultReg;
        }
    }
    
    public class NodeOutput extends NodeStmt {
        private NodeId id;
        public NodeOutput(NodeId id) { this.id = id; }
        @Override
        public void show() {
            System.out.println("AST output");
            id.show();
            System.out.println();
        }
        @Override
        public String createCode() {
            String reg = id.createCode();
            codeLines.add("printi " + reg);
            return "";
        }
    }
    
    public class NodeInitialize extends NodeStmt {
        private NodeId id;
        private NodeConstInt value;
        public NodeInitialize(NodeId id, NodeConstInt value) {
            this.id = id;
            this.value = value;
        }
        @Override
        public void show() {
            System.out.println("AST initialize");
            id.show(); value.show();
            System.out.println();
        }
        @Override
        public String createCode() {
            String valReg = value.createCode();
            String varName = id.getName();
            codeLines.add("storeintvar " + valReg + ", " + varName);
            varToRegister.put(varName, valReg);
            return "";
        }
    }
    
    public class NodeCompute extends NodeStmt {
        private NodeId id;
        private NodeExpr expr;
        public NodeCompute(NodeId id, NodeExpr expr) {
            this.id = id;
            this.expr = expr;
        }
        @Override
        public void show() {
            System.out.println("AST compute");
            id.show(); expr.show();
            System.out.println();
        }
        @Override
        public String createCode() {
            String exprReg = expr.createCode();
            String varName = id.getName();
            codeLines.add("storeintvar " + exprReg + ", " + varName);
            varToRegister.put(varName, exprReg);
            return "";
        }
    }
    
    public class NodeStmts extends NodeBase {
        private List<NodeStmt> statements = new ArrayList<>();
        public void addStmt(NodeStmt stmt) { statements.add(stmt); }
        @Override
        public void show() {
            System.out.println("\nAST Statements");
            for (NodeStmt s : statements) s.show();
        }
        @Override
        public String createCode() {
            for (NodeStmt s : statements) s.createCode();
            return "";
        }
    }
    
    public class NodeIf extends NodeStmt {
        private NodeId leftId;
        private NodeId rightId;
        private NodeStmts stmts;
        public NodeIf(NodeId leftId, NodeId rightId, NodeStmts stmts) {
            this.leftId = leftId;
            this.rightId = rightId;
            this.stmts = stmts;
        }
        @Override
        public void show() {
            System.out.println("AST if");
            System.out.print("LHS: "); leftId.show();
            System.out.print("RHS: "); rightId.show();
            System.out.println();
            for (NodeStmt s : stmts.statements) s.show();
            System.out.println("AST endif");
        }
        @Override
        public String createCode() {
            String leftReg = leftId.createCode();
            String rightReg = rightId.createCode();
            // generate a simple label without underscores
            String endIfLabel = getNextLabel("endif");
            codeLines.add("bne " + leftReg + ", " + rightReg + ", " + endIfLabel);
            stmts.createCode();
            codeLines.add(":"+endIfLabel);
            return "";
        }
    }
    
    public class NodeVars extends NodeBase {
        private List<NodeId> variables = new ArrayList<>();
        public void addVar(NodeId var) { variables.add(var); }
        @Override
        public void show() {
            System.out.println("AST Variables");
            for (NodeId v : variables) v.show();
        }
        @Override
        public String createCode() {
            for (NodeId v : variables) {
                codeLines.add("var int " + v.getName());
            }
            // switch to code section
            codeLines.add(".code");
            return "";
        }
    }
    
    public class NodeProgram extends NodeBase {
        private NodeVars vars;
        private NodeStmts stmts;
        public NodeProgram(NodeVars vars, NodeStmts stmts) {
            this.vars = vars;
            this.stmts = stmts;
        }
        @Override
        public void show() {
            vars.show(); stmts.show();
        }
        @Override
        public String createCode() {
            vars.createCode();
            stmts.createCode();
            return "";
        }
    }
}
