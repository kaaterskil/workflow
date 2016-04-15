package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EscalationEventDefinition extends EventDefinition {

    @XmlAttribute
    private String escalationRef;

    @Override
    public String toString() {
        return String.format(
                "EscalationEventDefinition [escalationRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                escalationRef, id, documentation, extensionElements);
    }

    public String getEscalationRef() {
        return escalationRef;
    }

    public void setEscalationRef(String escalationRef) {
        this.escalationRef = escalationRef;
    }
}
