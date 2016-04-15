package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Service Task is a Task that uses some sort of service, which could be a Web service or an
 * automated application.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ServiceTask extends Task {

    /**
     * This attribute specifies the technology that will be used to send and receive the Messages.
     * Valid values are "##unspecified" for leaving the implementation technology open,
     * "##WebService" for the Web service technology or a URI identifying any other technology or
     * coordination protocol. A Web service is the default technology.
     */
    @XmlAttribute
    private String implementation = "##webService";

    /**
     * This attribute specifies the operation that is invoked by the Service Task.
     */
    @XmlAttribute
    private String operationRef;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "ServiceTask [implementation=%s, operationRef=%s, isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                implementation, operationRef, isForCompensation, loopCharacteristics,
                defaultSequenceFlow, ioSpecification, properties, dataInputAssociations,
                dataOutputAssociations, startQuantity, completionQuantity, state, incoming,
                outgoing, name, categoryValueRef, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getOperationRef() {
        return operationRef;
    }

    public void setOperationRef(String operationRef) {
        this.operationRef = operationRef;
    }
}
