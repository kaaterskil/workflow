package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Receive Task is a simple Task that is designed to wait for a Message to arrive from an external
 * Participant (relative to the Process). Once the Message has been received, the Task is completed.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ReceiveTask extends Task {

    /**
     * A Message for the messageRef attribute MAY be entered. This indicates that the Message will
     * be received by the Task. The Message in this context is equivalent to an in-only message
     * pattern (Web service). One (1) or more corresponding incoming Message Flows MAY be shown on
     * the diagram. However, the display of the Message Flows is NOT REQUIRED. The Message is
     * applied to all incoming Message Flows, but can arrive for only one (1) of the incoming
     * Message Flows for a single instance of the Task.
     */
    @XmlAttribute
    private String messageRef;

    /**
     * Receive Tasks can be defined as the instantiation mechanism for the Process with the
     * instantiate attribute. This attribute MAY be set to true if the Task is the first Activity
     * (i.e., there are no incoming Sequence Flows). Multiple Tasks MAY have this attribute set to
     * true.
     */
    @XmlAttribute
    private boolean instantiate = false;

    /**
     * This attribute specifies the operation through which the Receive Task receives the Message.
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
                "ReceiveTask [messageRef=%s, instantiate=%s, operationRef=%s, implementation=%s, isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                messageRef, instantiate, operationRef, implementation, isForCompensation,
                loopCharacteristics, defaultSequenceFlow, ioSpecification, properties,
                dataInputAssociations, dataOutputAssociations, startQuantity, completionQuantity,
                state, incoming, outgoing, name, categoryValueRef, id, documentation,
                extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getMessageRef() {
        return messageRef;
    }

    public void setMessageRef(String messageRef) {
        this.messageRef = messageRef;
    }

    public boolean isInstantiate() {
        return instantiate;
    }

    public void setInstantiate(boolean instantiate) {
        this.instantiate = instantiate;
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
