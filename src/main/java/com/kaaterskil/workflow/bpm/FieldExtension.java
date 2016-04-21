package com.kaaterskil.workflow.bpm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class FieldExtension {

    @XmlAttribute(name = "name", required = true)
    private String fieldName;

    @XmlAttribute
    private String value;

    @XmlAttribute
    private String expression;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format("FieldExtension [fieldName=%s, value=%s, expression=%s]", fieldName,
                value, expression);
    }

    /*---------- Getter/Setters ----------*/

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
