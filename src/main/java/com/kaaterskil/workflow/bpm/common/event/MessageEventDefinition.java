package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MessageEventDefinition extends EventDefinition {

    @XmlAttribute
    private String messageRef;

    @XmlAttribute
    private String operationRef;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "MessageEventDefinition [messageRef=%s, operationRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                messageRef, operationRef, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getMessageRef() {
        return messageRef;
    }

    public void setMessageRef(String messageRef) {
        this.messageRef = messageRef;
    }

    public String getOperationRef() {
        return operationRef;
    }

    public void setOperationRef(String operationRef) {
        this.operationRef = operationRef;
    }
}
