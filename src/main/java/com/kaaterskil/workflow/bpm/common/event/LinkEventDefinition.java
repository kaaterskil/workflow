package com.kaaterskil.workflow.bpm.common.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class LinkEventDefinition extends EventDefinition {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private List<String> sources = new ArrayList<>();

    @XmlAttribute
    private String target;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "LinkEventDefinition [name=%s, sources=%s, target=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, sources, target, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
