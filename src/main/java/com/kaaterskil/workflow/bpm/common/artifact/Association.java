package com.kaaterskil.workflow.bpm.common.artifact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An Association is used to associate information and Artifacts with Flow Objects. Text and
 * graphical non-Flow Objects can be associated with the Flow Objects and Flow. An Association is
 * also used to show the Activity used for compensation. More information about compensation can be
 * found on page 302.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Association extends Artifact {

    /**
     * The BaseElement that the Association is connecting from.
     */
    @XmlAttribute
    private String sourceRef;

    /**
     * The BaseElement that the Association is connecting to.
     */
    @XmlAttribute
    private String targetRef;

    /**
     * associationDirection is an attribute that defines whether or not the Association shows any
     * directionality with an arrowhead. The default is None (no arrowhead). A value of One means
     * that the arrowhead SHALL be at the Target Object. A value of Both means that there SHALL be
     * an arrowhead at both ends of the Association line.
     */
    @XmlAttribute
    private AssociationDirection associationDirection = AssociationDirection.NONE;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Association [sourceRef=%s, targetRef=%s, associationDirection=%s, id=%s, documentation=%s, extensionElements=%s]",
                sourceRef, targetRef, associationDirection, id, documentation, extensionElements);
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

    public AssociationDirection getAssociationDirection() {
        return associationDirection;
    }

    public void setAssociationDirection(AssociationDirection associationDirection) {
        this.associationDirection = associationDirection;
    }
}
