package com.kaaterskil.workflow.bpm.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.RootElement;

/**
 * An Error represents the content of an Error Event or the Fault of a failed Operation. An
 * ItemDefinition is used to specify the structure of the Error. An Error is generated when there is
 * a critical problem in the processing of an Activity or when the execution of an Operation failed.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Error extends RootElement {

    /**
     * An ItemDefinition is used to define the “payload” of the Error.
     */
    @XmlAttribute
    private String structureRef;

    /**
     * The descriptive name of the Error.
     */
    @XmlAttribute
    private String name;

    /**
     * For an End Event: If the result is an Error, then the errorCode MUST be supplied (if the
     * processType attribute of the Process is set to execut- able) This “throws” the Error.
     * <p>
     * For an Intermediate Event within normal flow: If the trigger is an Error, then the errorCode
     * MUST be entered (if the processType attribute of the Process is set to execut- able). This
     * “throws” the Error.
     * <p>
     * For an Intermediate Event attached to the boundary of an Activity: If the trigger is an
     * Error, then the errorCode MAY be entered. This Event “catches” the Error. If there is no
     * errorCode, then any error SHALL trigger the Event. If there is an errorCode, then only an
     * Error that matches the errorCode SHALL trigger the Event.
     */
    @XmlAttribute
    private String errorCode;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Error [structureRef=%s, name=%s, errorCode=%s, id=%s, documentation=%s, extensionElements=%s]",
                structureRef, name, errorCode, id, documentation, extensionElements);
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
