package org.example;

import java.util.ArrayList;
import java.util.List;

public class AbsSynTree {

    private NodeProgram root;

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

    public abstract class NodeBase {
        public abstract void show();
    }

    public abstract class NodeExpr extends NodeBase {
        ; // Base class
    }

    public abstract class NodeStmt extends NodeBase {
        ; // Base class
    }

    public class NodeId extends NodeExpr {
        private String name;

        public NodeId(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public void show() {
            System.out.println("AST id " + name);
        }
    }

    public class NodeConstInt extends NodeExpr {
        private int value;

        public NodeConstInt(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public void show() {
            System.out.println("AST const int " + value);
        }
    }


    public class NodePlus extends NodeExpr {
        private NodeExpr left;
        private NodeExpr right;

        public NodePlus(NodeExpr left, NodeExpr right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void show() {
            System.out.println("AST plus");
            System.out.print("LHS: ");
            left.show();
            System.out.print("RHS: ");
            right.show();
        }
    }

    public class NodeOutput extends NodeStmt {
        private NodeId id;

        public NodeOutput(NodeId id) {
            this.id = id;
        }

        @Override
        public void show() {
            System.out.println("AST output");
            id.show();
            System.out.println();
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
            id.show();
            value.show();
            System.out.println();
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
            id.show();
            expr.show();
            System.out.println();
        }
    }

    public class NodeStmts extends NodeBase {
        private List<NodeStmt> statements;

        public NodeStmts() {
            this.statements = new ArrayList<>();
        }

        public void addStmt(NodeStmt stmt) {
            statements.add(stmt);
        }

        public List<NodeStmt> getStatements() {
            return statements;
        }

        @Override
        public void show() {
            System.out.println("\nAST Statements");
            for (NodeStmt stmt : statements) {
                stmt.show();
            }
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
            System.out.print("LHS: ");
            leftId.show();
            System.out.print("RHS: ");
            rightId.show();
            System.out.println();
            for (NodeStmt stmt : stmts.getStatements()) {
                stmt.show();
            }
            System.out.println("AST endif");

        }
    }

    public class NodeVars extends NodeBase {
        private List<NodeId> variables;

        public NodeVars() {
            this.variables = new ArrayList<>();
        }

        public void addVar(NodeId var) {
            variables.add(var);
        }

        public List<NodeId> getVariables() {
            return variables;
        }

        @Override
        public void show() {
            System.out.println("AST Variables");
            for (NodeId var : variables) {
                var.show();
            }
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
            vars.show();
            stmts.show();
        }
    }
}