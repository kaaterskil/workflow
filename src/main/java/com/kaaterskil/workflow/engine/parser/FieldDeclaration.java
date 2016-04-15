package com.kaaterskil.workflow.engine.parser;

public class FieldDeclaration {

    private String name;
    private String type;
    private Object value;

    public FieldDeclaration(String name, String type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public FieldDeclaration() {
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
