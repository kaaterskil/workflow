package com.kaaterskil.workflow.bpm.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The FlowNode element is used to provide a single element as the source and target Sequence Flow
 * associations (see Figure 8.35) instead of the individual associations of the elements that can
 * connect to Sequence Flows (see the section above). Only the Gateway, Activity, Choreography
 * Activity, and Event elements can connect to Sequence Flows and thus, these elements are the only
 * ones that are sub-classes of FlowNode.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class FlowNode extends FlowElement {

    /**
     * This attribute identifies the incoming Sequence Flow of the FlowNode.
     */
    @XmlElementWrapper(name = "incoming")
    @XmlElement(name = "sequenceFlow", type = SequenceFlow.class, required = false)
    protected List<SequenceFlow> incoming = new ArrayList<>();

    /**
     * This attribute identifies the outgoing Sequence Flow of the FlowNode. This is an ordered
     * collection.
     */
    @XmlElementWrapper(name = "outgoing")
    @XmlElement(name = "sequenceFlow", type = SequenceFlow.class, required = false)
    protected List<SequenceFlow> outgoing = new ArrayList<>();

    @XmlAttribute
    protected boolean isAsynchronous;

    @XmlAttribute
    protected String behavior;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "FlowNode [incoming=%s, outgoing=%s, isAsynchronous=%s, behavior=%s, name=%s, categoryValueRef=%s, tokenListeners=%s, subProcess=%s, id=%s, documentation=%s, extensionElements=%s, extensionAttributes=%s]",
                incoming, outgoing, isAsynchronous, behavior, name, categoryValueRef,
                tokenListeners, subProcess, id, documentation, extensionElements,
                extensionAttributes);
    }

    /*---------- Getter/Setters ----------*/

    public List<SequenceFlow> getIncoming() {
        return incoming;
    }

    public void setIncoming(List<SequenceFlow> incoming) {
        this.incoming = incoming;
    }

    public List<SequenceFlow> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(List<SequenceFlow> outgoing) {
        this.outgoing = outgoing;
    }

    public boolean isAsynchronous() {
        return isAsynchronous;
    }

    public void setAsynchronous(boolean isAsynchronous) {
        this.isAsynchronous = isAsynchronous;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}
