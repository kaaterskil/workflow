package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Business Rule Task provides a mechanism for the Process to provide input to a Business Rules
 * Engine and to get the output of calculations that the Business Rules Engine might provide. The
 * InputOutputSpecification of the Task (see page 211) will allow the Process to send data to and
 * receive data from the Business Rules Engine.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class BusinessRuleTask extends Task {

    /**
     * This attribute specifies the technology that will be used to implement the Business Rule
     * Task. Valid values are "##unspecified" for leaving the implementation technology open,
     * "##WebService" for the Web service technology or a URI identifying any other technology or
     * coordination protocol. The default technology for this task is unspecified.
     */
    @XmlAttribute
    private String implementation = "##unspecified";

    @Override
    public String toString() {
        return String.format(
                "BusinessRuleTest [implementation=%s, isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                implementation, isForCompensation, loopCharacteristics, defaultSequenceFlow,
                ioSpecification, properties, dataInputAssociations, dataOutputAssociations,
                startQuantity, completionQuantity, state, incoming, outgoing, name,
                categoryValueRef, id, documentation, extensionElements);
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }
}
