package com.kaaterskil.workflow.bpm.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class InputOutputBinding extends BaseElement {

    @XmlAttribute
    private String inputDataRef;

    @XmlAttribute
    private String outputDataRef;

    @XmlAttribute
    private String operation;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "InputOutputBinding [inputDataRef=%s, outputDataRef=%s, operation=%s, id=%s, documentation=%s, extensionElements=%s]",
                inputDataRef, outputDataRef, operation, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getInputDataRef() {
        return inputDataRef;
    }

    public void setInputDataRef(String inputDataRef) {
        this.inputDataRef = inputDataRef;
    }

    public String getOutputDataRef() {
        return outputDataRef;
    }

    public void setOutputDataRef(String outputDataRef) {
        this.outputDataRef = outputDataRef;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
