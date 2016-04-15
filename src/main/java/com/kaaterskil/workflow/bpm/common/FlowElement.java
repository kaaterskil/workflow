package com.kaaterskil.workflow.bpm.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.HasTokenListeners;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;

/**
 * FlowElement is the abstract super class for all elements that can appear in a Process flow, which
 * are FlowNodes (see page 99, which consist of Activities (see page 151), Choreography Activities
 * (see page 321) Gateways (see page 287), and Events (see page 233), Data Objects (see page 205),
 * Data Associations (see page 221), and Sequence Flows (see page 97).
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class FlowElement extends BaseElement implements HasTokenListeners {

    /**
     * The descriptive name of the element.
     */
    @XmlAttribute
    protected String name;

    /**
     * A reference to the Category Values that are associated with this Flow Element.
     */
    @XmlElement(name = "categoryValueRef", required = false)
    protected List<String> categoryValueRef = new ArrayList<>();

    @XmlElement(name = "listener", type = Listener.class, required = false)
    protected List<Listener> tokenListeners = new ArrayList<>();

    @XmlElement(name = "subProcess", type = SubProcess.class, required = false)
    protected SubProcess subProcess;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "FlowElement [name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, categoryValueRef, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategoryValueRef() {
        return categoryValueRef;
    }

    public void setCategoryValueRef(List<String> categoryValueRef) {
        this.categoryValueRef = categoryValueRef;
    }

    @Override
    public List<Listener> getTokenListeners() {
        return tokenListeners;
    }

    @Override
    public void setTokenListeners(List<Listener> tokenListeners) {
        this.tokenListeners = tokenListeners;
    }

    public SubProcess getSubProcess() {
        return subProcess;
    }

    public void setSubProcess(SubProcess subProcess) {
        this.subProcess = subProcess;
    }
}
