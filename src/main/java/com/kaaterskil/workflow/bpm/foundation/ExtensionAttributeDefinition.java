package com.kaaterskil.workflow.bpm.foundation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ExtensionAttributeDefinition {

    /**
     * The name of the extension attribute.
     */
    @XmlAttribute
    private String name;

    /**
     * The type that is associated with the attribute.
     */
    @XmlAttribute
    private String type;

    /**
     * Indicates if the attribute value will be referenced or contained.
     */
    @XmlAttribute
    private boolean isReference = false;

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

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean isReference) {
        this.isReference = isReference;
    }
}
