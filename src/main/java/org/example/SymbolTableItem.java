package org.example;

import java.lang.reflect.Type;

public class SymbolTableItem {
    private String name;
    private Parser.TYPE type;

    public SymbolTableItem(String name, Parser.TYPE type) {
        this.name = name;
        this.type = type;
    }

    public String getName(){ return name;}
    public void setName(String name) { this.name = name;}

    public Parser.TYPE getType() { return type; }
    @Override
    public String toString() {
        return "SymbolTableItem{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
