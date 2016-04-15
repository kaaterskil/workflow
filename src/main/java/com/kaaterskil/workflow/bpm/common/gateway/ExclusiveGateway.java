package com.kaaterskil.workflow.bpm.common.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A diverging Exclusive Gateway (Decision) is used to create alternative paths within a Process
 * flow. This is basically the “diversion point in the road” for a Process. For a given instance of
 * the Process, only one of the paths can be taken.
 * <p>
 * A Decision can be thought of as a question that is asked at a particular point in the Process.
 * The question has a defined set of alternative answers. Each answer is associated with a condition
 * Expression that is associated with a Gateway’s outgoing Sequence Flows.
 * <p>
 * A default path can optionally be identified, to be taken in the event that none of the
 * conditional Expressions evaluate to true. If a default path is not specified and the Process is
 * executed such that none of the conditional Expressions evaluates to true, a runtime exception
 * occurs.
 * <p>
 * A converging Exclusive Gateway is used to merge alternative paths. Each incoming Sequence Flow
 * token is routed to the outgoing Sequence Flow without synchronization.
 *
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ExclusiveGateway extends Gateway {

    /**
     * The Sequence Flow that will receive a token when none of the conditionExpressions on other
     * outgoing Sequence Flows evaluate to true. The default Sequence Flow should not have a
     * conditionExpression. Any such Expression SHALL be ignored.
     */
    @XmlAttribute(name = "default", required = false)
    private String defaultSequenceFlow;

    @Override
    public String toString() {
        return String.format(
                "ExclusiveGateway [defaultSequenceFlow=%s, gatewayDirection=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                defaultSequenceFlow, gatewayDirection, incoming, outgoing, name, categoryValueRef, id,
                documentation, extensionElements);
    }

    public String getDefaultSequenceFlow() {
        return defaultSequenceFlow;
    }

    public void setDefaultSequenceFlow(String defaultSequenceFlow) {
        this.defaultSequenceFlow = defaultSequenceFlow;
    }

}
