package com.kaaterskil.workflow.bpm.common.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.common.Expression;

/**
 * The Complex Gateway can be used to model complex synchronization behavior. An Expression
 * activationCondition is used to describe the precise behavior. For example, this Expression could
 * specify that tokens on three out of five incoming Sequence Flows are needed to activate the
 * Gateway. What tokens are produced by the Gateway is determined by conditions on the outgoing
 * Sequence Flows as in the split behavior of the Inclusive Gateway. If tokens arrive later on the
 * two remaining Sequence Flows, those tokens cause a reset of the Gateway and new token can be
 * produced on the outgoing Sequence Flows. To determine whether it needs to wait for additional
 * tokens before it can reset, the Gateway uses the synchronization semantics of the Inclusive
 * Gateway.
 * <p>
 * The Complex Gateway has, in contrast to other Gateways, an internal state, which is represented
 * by the boolean instance attribute waitingForStart, which is initially true and becomes false
 * after activation. This attribute can be used in the conditions of the outgoing Sequence Flows to
 * specify where tokens are produced upon activation and where tokens are produced upon reset. It is
 * RECOMMENDED that each outgoing Sequence Flow either get a token upon activation or upon reset but
 * not both. At least one outgoing Sequence Flow should receive a token upon activation but a token
 * MUST NOT be produced upon reset.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ComplexGateway extends Gateway {

    /**
     * Determines which combination of incoming tokens will be synchronized for activation of the
     * Gateway.
     */
    @XmlElement(name = "activationCondition", type = Expression.class, required = false)
    private Expression activationCondition;

    /**
     * The Sequence Flow that will receive a token when none of the conditionExpressions on other
     * Sequence Flows evaluate to true. The default Sequence Flow should not have a
     * conditionExpression. Any such Expression SHALL be ignored.
     */
    @XmlAttribute(name = "default")
    private String defaultSequenceFlow;

    /**
     * Refers at runtime to the number of tokens that are present on an incoming Sequence Flow of
     * the Complex Gateway.
     */
    @XmlTransient
    private int activationCount;

    /**
     * Represents the internal state of the Complex Gateway. It is either waiting for start (=true)
     * or waiting for reset (=false).
     */
    @XmlTransient
    private boolean waitingForStart = true;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "ComplexGateway [activationCondition=%s, defaultSequenceFlow=%s, activationCount=%s, waitingForStart=%s, gatewayDirection=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                activationCondition, defaultSequenceFlow, activationCount, waitingForStart,
                gatewayDirection, incoming, outgoing, name, categoryValueRef, id, documentation,
                extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public Expression getActivationCondition() {
        return activationCondition;
    }

    public void setActivationCondition(Expression activationCondition) {
        this.activationCondition = activationCondition;
    }

    public String getDefaultSequenceFlow() {
        return defaultSequenceFlow;
    }

    public void setDefaultSequenceFlow(String defaultSequenceFlow) {
        this.defaultSequenceFlow = defaultSequenceFlow;
    }

    public int getActivationCount() {
        return activationCount;
    }

    public void setActivationCount(int activationCount) {
        this.activationCount = activationCount;
    }

    public boolean isWaitingForStart() {
        return waitingForStart;
    }

    public void setWaitingForStart(boolean waitingForStart) {
        this.waitingForStart = waitingForStart;
    }
}
