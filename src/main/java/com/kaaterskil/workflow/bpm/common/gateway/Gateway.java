package com.kaaterskil.workflow.bpm.common.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.FlowNode;

/**
 * Gateways are used to control how the Process flows (how Tokens flow) through Sequence Flows as
 * they converge and diverge within a Process. If the flow does not need to be controlled, then a
 * Gateway is not needed. The term “gateway” implies that there is a gating mechanism that either
 * allows or disallows passage through the Gateway--that is, as tokens arrive at a Gateway, they can
 * be merged together on input and/or split apart on output as the Gateway mechanisms are invoked.
 * <p>
 * Gateways, like Activities, are capable of consuming or generating additional control tokens,
 * effectively controlling the execution semantics of a given Process. The main difference is that
 * Gateways do not represent ‘work’ being done and they are considered to have zero effect on the
 * operational measures of the Process being executed (cost, time, etc.).
 * <p>
 * The Gateway controls the flow of both diverging and converging Sequence Flows. That is, a single
 * Gateway could have multiple input and multiple output flows. Modelers and modeling tools might
 * want to enforce a best practice of a Gateway only performing one of these functions. Thus, it
 * would take two sequential Gateways to first converge and then to diverge the Sequence Flows.
 * <p>
 * The Gateway class is an abstract type. Its concrete subclasses define the specific semantics of
 * individual Gateway types, defining how the Gateway behaves in different situations.
 * </p>
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Gateway extends FlowNode {

    /**
     * An attribute that adds constraints on how the Gateway MAY be used.
     * <ul>
     * <li>Unspecified: There are no constraints. The Gateway MAY have any number of incoming and
     * outgoing Sequence Flows.
     * <li>Converging: This Gateway MAY have multiple incoming Sequence Flows but MUST have no more
     * than one (1) outgoing Sequence Flow.
     * <li>Diverging: This Gateway MAY have multiple outgoing Sequence Flows but MUST have no more
     * than one (1) incoming Sequence Flow.
     * <li>Mixed: This Gateway contains multiple outgoing and multiple incoming Sequence Flows.
     * </ul>
     */
    @XmlAttribute
    protected GatewayDirection gatewayDirection;

    public GatewayDirection getGatewayDirection() {
        return gatewayDirection;
    }

    public void setGatewayDirection(GatewayDirection gatewayDirection) {
        this.gatewayDirection = gatewayDirection;
    }
}
