package com.kaaterskil.workflow.bpm.common.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.data.Property;

/**
 * An Event is something that “happens” during the course of a Process. These Events affect the flow
 * of the Process and usually have a cause or an impact. The term “event” is general enough to cover
 * many things in a Process. The start of an Activity, the end of an Activity, the change of state
 * of a document, a Message that arrives, etc., all could be considered Events. However, BPMN has
 * restricted the use of Events to include only those types of Events that will affect the sequence
 * or timing of Activities of a Process.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Event extends FlowNode {

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property", type = Property.class)
    protected List<Property> properties = new ArrayList<>();

    /*---------- Getter/Setters ----------*/

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
