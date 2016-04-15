package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.RootElement;

/**
 * An Escalation identifies a business situation that a Process might need to react to. An
 * ItemDefinition is used to specify the structure of the Escalation.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Escalation extends RootElement {

    /**
     * An ItemDefinition is used to define the “payload” of the Escalation.
     */
    @XmlAttribute
    private String structureRef;

    /**
     * The descriptive name of the Escalation.
     */
    @XmlAttribute
    private String name;

    /**
     * For an End Event: If the Result is an Escalation, then the escalationCode MUST be supplied
     * (if the processType attribute of the Process is set to executable). This “throws” the
     * Escalation.
     * <p>
     * For an Intermediate Event within normal flow: If the trigger is an Escalation, then the
     * escalationCode MUST be entered (if the processType attribute of the Process is set to
     * executable). This “throws” the Escalation.
     * <p>
     * For an Intermediate Event attached to the boundary of an Activity: If the trigger is an
     * Escalation, then the escalationCode MAY be entered. This Event “catches” the Escalation. If
     * there is no escalationCode, then any Escalation SHALL trigger the Event. If there is an
     * escalationCode, then only an Escalation that matches the escalationCode SHALL trigger the
     * Event.
     */
    @XmlAttribute
    private String escalationCode;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Escalation [structureRef=%s, name=%s, escalationCode=%s, id=%s, documentation=%s, extensionElements=%s]",
                structureRef, name, escalationCode, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getStructureRef() {
        return structureRef;
    }

    public void setStructureRef(String structureRef) {
        this.structureRef = structureRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEscalationCode() {
        return escalationCode;
    }

    public void setEscalationCode(String escalationCode) {
        this.escalationCode = escalationCode;
    }
}
