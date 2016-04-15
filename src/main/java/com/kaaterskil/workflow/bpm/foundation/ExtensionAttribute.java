package com.kaaterskil.workflow.bpm.foundation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The ExtensionAttributeValue contains the attribute value.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ExtensionAttribute {

    /**
     * The name of the extension attribute.
     */
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String type;

    /**
     * The contained attribute value, used when the associated
     * ExtensionAttributeDefinition.isReference is false. The type of this Element MUST conform to
     * the type specified in the associated ExtensionAttributeDefinition.
     */
    @XmlAttribute
    private String value;

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("ExtensionAttribute [name=%s, value=%s]", name, value);
    }
}
