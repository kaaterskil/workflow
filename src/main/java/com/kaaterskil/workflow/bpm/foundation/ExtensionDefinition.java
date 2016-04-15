package com.kaaterskil.workflow.bpm.foundation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ExtensionDefinition {

    @XmlAttribute
    private String name;

    @XmlElement(name = "extensionAttributeDefinitions", type = ExtensionAttributeDefinition.class, required = false)
    private List<ExtensionAttributeDefinition> extensionAttributeDefinitions = new ArrayList<>();

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExtensionAttributeDefinition> getExtensionAttributeDefinitions() {
        return extensionAttributeDefinitions;
    }

    public void setExtensionAttributeDefinitions(
            List<ExtensionAttributeDefinition> extensionAttributeDefinitions) {
        this.extensionAttributeDefinitions = extensionAttributeDefinitions;
    }
}
