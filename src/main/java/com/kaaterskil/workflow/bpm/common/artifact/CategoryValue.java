package com.kaaterskil.workflow.bpm.common.artifact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CategoryValue extends BaseElement {

    /**
     * This attribute provides the value of the CategoryValue element.
     */
    @XmlAttribute
    private String value;

    @Override
    public String toString() {
        return String.format(
                "CategoryValue [value=%s, id=%s, documentation=%s, extensionElements=%s]", value,
                id, documentation, extensionElements);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
