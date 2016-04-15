package com.kaaterskil.workflow.bpm.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

/**
 * An Operation defines Messages that are consumed and, optionally, produced when the Operation is
 * called. It can also define zero or more errors that are returned when operation fails. The
 * Operation inherits the attributes and model associations of BaseElement (see Table 8.5). Table
 * 8.66 below presents the additional attributes and model associations of the Operation.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Operation extends BaseElement {

    /**
     * The descriptive name of the element.
     */
    @XmlAttribute
    private String name;

    /**
     * This attribute specifies the input Message of the Operation. An Operation has exactly one
     * input Message.
     */
    @XmlAttribute
    private String inMessageRef;

    /**
     * This attribute specifies the output Message of the Operation. An Operation has at most one
     * input Message.
     */
    @XmlAttribute
    private String outMessageRef;

    /**
     * This attribute allows to reference a concrete artifact in the underlying implementation
     * technology representing that operation, such as a WSDL operation.
     */
    @XmlAttribute
    private String implementationRef;

    /**
     * This attribute specifies errors that the Operation may return. An Operation MAY refer to zero
     * or more Error elements.
     */
    @XmlAttribute
    private List<String> errorRefs = new ArrayList<>();

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Operation [name=%s, inMessageRef=%s, outMessageRef=%s, implementationRef=%s, errorMessageRefs=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, inMessageRef, outMessageRef, implementationRef, errorRefs, id, documentation,
                extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInMessageRef() {
        return inMessageRef;
    }

    public void setInMessageRef(String inMessageRef) {
        this.inMessageRef = inMessageRef;
    }

    public String getOutMessageRef() {
        return outMessageRef;
    }

    public void setOutMessageRef(String outMessageRef) {
        this.outMessageRef = outMessageRef;
    }

    public String getImplementationRef() {
        return implementationRef;
    }

    public void setImplementationRef(String implementationRef) {
        this.implementationRef = implementationRef;
    }

    public List<String> getErrorRefs() {
        return errorRefs;
    }

    public void setErrorRefs(List<String> errorRefs) {
        this.errorRefs = errorRefs;
    }
}
