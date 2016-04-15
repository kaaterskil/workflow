package com.kaaterskil.workflow.bpm.common.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class InputSet extends BaseElement {

    @XmlAttribute
    private String name;

    @XmlElement(required = false)
    private List<String> dataInputRefs = new ArrayList<>();

    @XmlElement(required = false)
    private List<String> outputSetRefs = new ArrayList<>();

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "InputSet [name=%s, dataInputRefs=%s, outputSetRefs=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, dataInputRefs, outputSetRefs, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDataInputRefs() {
        return dataInputRefs;
    }

    public void setDataInputRefs(List<String> dataInputRefs) {
        this.dataInputRefs = dataInputRefs;
    }

    public List<String> getOutputSetRefs() {
        return outputSetRefs;
    }

    public void setOutputSetRefs(List<String> outputSetRefs) {
        this.outputSetRefs = outputSetRefs;
    }
}
