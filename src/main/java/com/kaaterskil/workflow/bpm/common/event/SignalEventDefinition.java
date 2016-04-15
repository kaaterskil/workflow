package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SignalEventDefinition extends EventDefinition {

    @XmlAttribute
    private String signalRef;

    @Override
    public String toString() {
        return String.format(
                "SignalEventDefinition [signalRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                signalRef, id, documentation, extensionElements);
    }

    public String getSignalRef() {
        return signalRef;
    }

    public void setSignalRef(String signalRef) {
        this.signalRef = signalRef;
    }
}
