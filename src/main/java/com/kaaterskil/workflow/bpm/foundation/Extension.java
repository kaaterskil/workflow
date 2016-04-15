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
public class Extension {

    @XmlAttribute
    private boolean mustUnderstand = false;

    @XmlAttribute
    private ExtensionDefinition definition;

    @XmlElement(name = "documentation", type = Documentation.class, required = false)
    private List<Documentation> documentation = new ArrayList<>();

    /*---------- Getter/Setters ----------*/

    public boolean isMustUnderstand() {
        return mustUnderstand;
    }

    public void setMustUnderstand(boolean mustUnderstand) {
        this.mustUnderstand = mustUnderstand;
    }

    public ExtensionDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ExtensionDefinition definition) {
        this.definition = definition;
    }

    public List<Documentation> getDocumentation() {
        return documentation;
    }

    public void setDocumentation(List<Documentation> documentation) {
        this.documentation = documentation;
    }
}
