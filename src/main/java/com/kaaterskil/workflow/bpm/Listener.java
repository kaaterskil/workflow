package com.kaaterskil.workflow.bpm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Listener extends BaseElement {

    @XmlAttribute
    private String eventRef;

    @XmlAttribute
    private String implementationType;

    @XmlAttribute
    private String implementation;

    @XmlTransient
    private Object instance;

    /*---------- Getter/Setters ----------*/

    public String getEventRef() {
        return eventRef;
    }

    public void setEventRef(String eventRef) {
        this.eventRef = eventRef;
    }

    public String getImplementationType() {
        return implementationType;
    }

    public void setImplementationType(String implementationType) {
        this.implementationType = implementationType;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
