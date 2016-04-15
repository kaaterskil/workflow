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
public class OutputSet extends BaseElement {

    @XmlAttribute
    private String name;

    @XmlElement(required = false)
    private List<String> dataOutputRefs = new ArrayList<>();

    @XmlElement(required = false)
    private List<String> inputSetRefs = new ArrayList<>();

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "OutputSet [name=%s, dataOutputRefs=%s, inputSetRefs=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, dataOutputRefs, inputSetRefs, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDataOutputRefs() {
        return dataOutputRefs;
    }

    public void setDataOutputRefs(List<String> dataOutputRefs) {
        this.dataOutputRefs = dataOutputRefs;
    }

    public List<String> getInputSetRefs() {
        return inputSetRefs;
    }

    public void setInputSetRefs(List<String> inputSetRefs) {
        this.inputSetRefs = inputSetRefs;
    }

}
