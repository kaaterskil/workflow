package com.kaaterskil.workflow.engine.parser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "signal")
@XmlAccessorType(XmlAccessType.NONE)
public class SignalDefinition {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format("SignalDefinition [id=%s, name=%s]", id, name);
    }

    /*---------- Getter/Setters ----------*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
