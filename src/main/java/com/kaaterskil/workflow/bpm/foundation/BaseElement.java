package com.kaaterskil.workflow.bpm.foundation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

/**
 * BaseElement is the abstract super class for most BPMN elements. It provides the attributes id and
 * documentation, which other elements will inherit.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class BaseElement {

    /**
     * This attribute is used to uniquely identify BPMN elements. The id is REQUIRED if this element
     * is referenced or intended to be referenced by something else. If the element is not currently
     * referenced and is never intended to be referenced, the id MAY be omitted.
     */
    @XmlAttribute
    protected String id;

    /**
     * This attribute is used to annotate the BPMN element, such as descriptions and other
     * documentation.
     */
    @XmlElement(name = "documentation", type = Documentation.class, required = false)
    protected List<Documentation> documentation = new ArrayList<>();

    /**
     * This attribute is used to attach additional attributes and associations to any BaseElement.
     * This association is not applicable when the XML schema interchange is used, since the XSD
     * mechanisms for supporting anyAttribute and any element already satisfy this requirement. See
     * page 57 for additional information on extensibility.
     */
    @XmlElementWrapper(name = "extensionElements", required = false)
    @XmlAnyElement(lax = true)
    protected List<Object> extensionElements = new ArrayList<>();

    /**
     * This attribute is used to provide values for extended attributes and model associations. This
     * association is not applicable when the XML schema interchange is used, since the XSD
     * mechanisms for supporting anyAttribute and any element already satisfy this requirement. See
     * page 57 for additional information on extensibility.
     */
    @XmlAnyAttribute
    protected Map<QName, Object> extensionAttributes = new HashMap<>();

    /*---------- Getter/Setters ----------*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Documentation> getDocumentation() {
        return documentation;
    }

    public void setDocumentation(List<Documentation> documentation) {
        this.documentation = documentation;
    }

    public List<Object> getExtensionElements() {
        return extensionElements;
    }

    public void setExtensionElements(List<Object> extensionElements) {
        this.extensionElements = extensionElements;
    }

    public Map<QName, Object> getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(Map<QName, Object> extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }
}
