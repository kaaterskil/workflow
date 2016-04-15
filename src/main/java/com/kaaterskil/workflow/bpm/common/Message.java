package com.kaaterskil.workflow.bpm.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.RootElement;

/**
 * A Message represents the content of a communication between two Participants. In BPMN 2.0, a
 * Message is a graphical decorator (it was a supporting element in BPMN 1.2). An ItemDefinition is
 * used to specify the Message structure.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Message extends RootElement {

    /**
     * Name is a text description of the Message.
     */
    @XmlAttribute
    private String name;

    /**
     * An ItemDefinition is used to define the “payload” of the Message.
     */
    @XmlAttribute
    private String itemRef;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Message [name=%s, itemRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, itemRef, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemRef() {
        return itemRef;
    }

    public void setItemRef(String itemRef) {
        this.itemRef = itemRef;
    }
}
