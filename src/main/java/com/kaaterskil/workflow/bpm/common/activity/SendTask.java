package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Send Task is a simple Task that is designed to send a Message to an external Participant
 * (relative to the Process). Once the Message has been sent, the Task is completed.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SendTask extends Task {

    /**
     * A Message for the messageRef attribute MAY be entered. This indicates that the Message will
     * be sent by the Task. The Message in this context is equivalent to an out-only message pattern
     * (Web service). One or more corresponding outgoing Message Flows MAY be shown on the diagram.
     * However, the display of the Message Flows is NOT REQUIRED. The Message is applied to all
     * outgoing Message Flows and the Message will be sent down all outgoing Message Flows at the
     * completion of a single instance of the Task.
     */
    @XmlAttribute
    private String messageRef;

    /**
     * This attribute specifies the operation that is invoked by the Send Task.
     */
    @XmlAttribute
    private String operationRef;

    /**
     * This attribute specifies the technology that will be used to send and receive the Messages.
     * Valid values are "##unspecified" for leaving the implementation technology open,
     * "##WebService" for the Web service technology or a URI identifying any other technology or
     * coordination protocol A Web service is the default technology.
     */
    @XmlAttribute
    private String implementation = "##webService";

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "SendTask [messageRef=%s, operationRef=%s, implementation=%s, isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                messageRef, operationRef, implementation, isForCompensation, loopCharacteristics,
                defaultSequenceFlow, ioSpecification, properties, dataInputAssociations,
                dataOutputAssociations, startQuantity, completionQuantity, state, incoming,
                outgoing, name, categoryValueRef, id, documentation, extensionElements);
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

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }
}
