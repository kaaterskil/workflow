package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.RootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Signal extends RootElement {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String structureRef;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Signal [name=%s, structureRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, structureRef, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStructureRef() {
        return structureRef;
    }

    public void setStructureRef(String structureRef) {
        this.structureRef = structureRef;
    }
}
