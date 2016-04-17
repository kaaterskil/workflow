package com.kaaterskil.workflow.bpm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Listener extends BaseElement {

    /**
     * Comma-delimited string of applicable WorkflowEventType values.
     */
    @XmlAttribute(name = "events", required = false)
    private String eventRefs;

    @XmlAttribute
    private ImplementationType implementationType = ImplementationType.CLASS;

    /**
     * The listener bean className
     */
    @XmlAttribute
    private String implementation;

    /**
     * The type of entity for which to trigger the event, i.e. token, job, process-definition, etc.
     */
    @XmlAttribute
    private String entityType;

    @XmlTransient
    private Object instance;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Listener [eventRefs=%s, implementationType=%s, implementation=%s, entityType=%s, instance=%s]",
                eventRefs, implementationType, implementation, entityType, instance);
    }

    /*---------- Getter/Setters ----------*/

    public String getEventRefs() {
        return eventRefs;
    }

    public void setEventRefs(String eventRefs) {
        this.eventRefs = eventRefs;
    }

    public ImplementationType getImplementationType() {
        return implementationType;
    }

    public void setImplementationType(ImplementationType implementationType) {
        this.implementationType = implementationType;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
