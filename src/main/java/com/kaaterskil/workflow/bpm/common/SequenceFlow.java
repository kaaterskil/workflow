package com.kaaterskil.workflow.bpm.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A Sequence Flow is used to show the order of Flow Elements in a Process or a Choreography. Each
 * Sequence Flow has only one source and only one target. The source and target MUST be from the set
 * of the following FlowElements: Events (Start, Intermediate, and End), Activities (Task and
 * Sub-Process; for Processes), Choreography Activities (Choreography Task and Sub-Choreography; for
 * Choreographies), and Gateways.
 * <p>
 * A Sequence Flow can optionally define a condition Expression, indicating that the token will be
 * passed down the Sequence Flow only if the Expression evaluates to true. This Expression is
 * typically used when the source of the Sequence Flow is a Gateway or an Activity.
 * <ul>
 * <li>If a conditional Sequence Flow is used from a source Activity, then there MUST be at least
 * one other outgoing Sequence Flow from that Activity.
 * <li>A source Gateway MUST NOT be of type Parallel or Event.
 * </ul>
 * A Sequence Flow that has an Exclusive, Inclusive, or Complex Gateway or an Activity as its source
 * can also be defined with as default. Such a Sequence Flow will have a marker to show that it is a
 * default flow. The default Sequence Flow is taken (a token is passed) only if all the other
 * outgoing Sequence Flows from the Activity or Gateway are not valid (i.e., their condition
 * Expressions are false).
 * </p>
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SequenceFlow extends FlowElement {

    /**
     * The FlowNode that the Sequence Flow is connecting from. For a Process: Of the types of
     * FlowNode, only Activities, Gateways, and Events can be the source. However, Activities that
     * are Event Sub-Processes are not allowed to be a source. For a Choreography: Of the types of
     * FlowNode, only Choreography Activities, Gateways, and Events can be the source.
     */
    @XmlAttribute(required = true)
    private String sourceRef;

    /**
     * The FlowNode that the Sequence Flow is connecting to. For a Process: Of the types of
     * FlowNode, only Activities, Gateways, and Events can be the target. However, Activities that
     * are Event Sub-Processes are not allowed to be a target. For a Choreography: Of the types of
     * FlowNode, only Choreography Activities, Gateways, and Events can be the target.
     */
    @XmlAttribute(required = true)
    private String targetRef;

    /**
     * An optional boolean value specifying whether Activities or Choreography Activities not in the
     * model containing the Sequence Flow can occur between the elements connected by the Sequence
     * Flow. If the value is true, they MAY NOT occur. If the value is false, they MAY occur. Also
     * see the isClosed attribute on Process, Choreography, and Collaboration. When the attribute
     * has no value, the default semantics depends on the kind of model containing Sequence Flows: •
     * <ul>
     * <li>For non-executable Processes (public Processes and non-executable private Processes) and
     * Choreographies no value has the same semantics as if the value were false.
     * <li>For an executable Processes no value has the same semantics as if the value were true.
     * <li>For executable Processes, the attribute MUST NOT be false.
     */
    @XmlAttribute(required = false)
    private Boolean isImmediate;

    /**
     * An optional boolean Expression that acts as a gating condition. A token will only be placed
     * on this Sequence Flow if this conditionExpression evaluates to true.
     */
    @XmlElement(name = "conditionExpression", type = Expression.class, required = false)
    private Expression conditionExpression;

    @XmlTransient
    private FlowElement sourceFlowElement;

    @XmlTransient
    private FlowElement targetFlowElement;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "SequenceFlow [sourceRef=%s, targetRef=%s, isImmediate=%s, conditionExpression=%s, name=%s, categoryValueRef=%s, tokenListeners=%s, subProcess=%s, id=%s, documentation=%s, extensionElements=%s, extensionAttributes=%s]",
                sourceRef, targetRef, isImmediate, conditionExpression, name, categoryValueRef,
                tokenListeners, subProcess, id, documentation, extensionElements,
                extensionAttributes);
    }

    /*---------- Getter/Setters ----------*/

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public Expression getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(Expression conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public Boolean getIsImmediate() {
        return isImmediate;
    }

    public void setImmediate(Boolean isImmediate) {
        this.isImmediate = isImmediate;
    }

    public FlowElement getSourceFlowElement() {
        return sourceFlowElement;
    }

    public void setSourceFlowElement(FlowElement sourceFlowElement) {
        this.sourceFlowElement = sourceFlowElement;
    }

    public FlowElement getTargetFlowElement() {
        return targetFlowElement;
    }

    public void setTargetFlowElement(FlowElement targetFlowElement) {
        this.targetFlowElement = targetFlowElement;
    }
}
