package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ErrorEventDefinition extends EventDefinition {

    @XmlAttribute
    private String errorRef;

    @Override
    public String toString() {
        return String.format(
                "ErrorEventDefinition [errorRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                errorRef, id, documentation, extensionElements);
    }

    public String getErrorRef() {
        return errorRef;
    }

    public void setErrorRef(String errorRef) {
        this.errorRef = errorRef;
    }
}
