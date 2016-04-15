package com.kaaterskil.workflow.bpm.common.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A diverging Inclusive Gateway (Inclusive Decision) can be used to create alternative but also
 * parallel paths within a Process flow. Unlike the Exclusive Gateway, all condition Expressions are
 * evaluated. The true evaluation of one condition Expression does not exclude the evaluation of
 * other condition Expressions. All Sequence Flows with a true evaluation will be traversed by a
 * token. Since each path is considered to be independent, all combinations of the paths MAY be
 * taken, from zero to all. However, it should be designed so that at least one path is taken.
 * <p>
 * A default path can optionally be identified, to be taken in the event that none of the
 * conditional Expressions evaluate to true. If a default path is not specified and the Process is
 * executed such that none of the conditional Expressions evaluates to true, a runtime exception
 * occurs.
 * <p>
 * A converging Inclusive Gateway is used to merge a combination of alternative and parallel paths.
 * A control flow token arriving at an Inclusive Gateway MAY be synchronized with some other tokens
 * that arrive later at this Gateway. The precise synchronization behavior of the Inclusive Gateway
 * can be found on page 292.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class InclusiveGateway extends Gateway {

    /**
     * The Sequence Flow that will receive a token when none of the conditionExpressions on other
     * Sequence Flows evaluate to true. The default Sequence Flow should not have a
     * conditionExpression. Any such Expression SHALL be ignored.
     */
    @XmlAttribute(name = "default", required = false)
    private String defaultSequenceFlow;

    @Override
    public String toString() {
        return String.format(
                "InclusiveGateway [defaultSequenceFlow=%s, gatewayDirection=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
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
