package com.kaaterskil.workflow.bpm.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.data.InputOutputSpecification;
import com.kaaterskil.workflow.bpm.foundation.RootElement;

/**
 * CallableElement is the abstract super class of all Activities that have been defined outside of a
 * Process or Choreography but which can be called (or reused), by a Call Activity, from within a
 * Process or Choreography. It MAY reference Interfaces that define the service operations that it
 * provides. The BPMN elements that can be called by Call Activities (i.e., are CallableElements)
 * are: Process and GlobalTask (see Figure 10.43). CallableElements are RootElements, which can be
 * imported and used in other Definitions. When CallableElements (e.g., Process) are defined, they
 * are contained within Definitions.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CallableElement extends RootElement {

    /**
     * The descriptive name of the element.
     */
    @XmlAttribute
    protected String name;

    /**
     * The InputOutputSpecification defines the inputs and outputs and the InputSets and OutputSets
     * for the Activity.
     */
    @XmlElement(name = "ioSpecification", type = InputOutputSpecification.class)
    protected InputOutputSpecification ioSpecification;

    /**
     * The InputOutputBinding defines a combination of one InputSet and one OutputSet in order to
     * bind this to an operation defined in an interface.
     */
    @XmlElementWrapper(name = "ioBinding")
    @XmlElement(name = "inputOutputBinding", type = InputOutputBinding.class)
    protected List<InputOutputBinding> ioBinding = new ArrayList<>();

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputOutputSpecification getIoSpecification() {
        return ioSpecification;
    }

    public void setIoSpecification(InputOutputSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }

    public List<InputOutputBinding> getIoBinding() {
        return ioBinding;
    }

    public void setIoBinding(List<InputOutputBinding> ioBinding) {
        this.ioBinding = ioBinding;
    }
}
