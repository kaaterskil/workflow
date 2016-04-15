package com.kaaterskil.workflow.bpm.common.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class DataState extends BaseElement {

    @XmlAttribute
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("DataState [name=%s, id=%s, documentation=%s, extensionElements=%s]", name,
                id, documentation, extensionElements);
    }
}
