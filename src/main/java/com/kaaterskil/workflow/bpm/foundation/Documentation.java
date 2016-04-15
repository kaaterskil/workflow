package com.kaaterskil.workflow.bpm.foundation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * All BPMN elements that inherit from the BaseElement will have the capability, through the
 * Documentation element, to have one (1) or more text descriptions of that element.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Documentation extends BaseElement {

    /**
     * This attribute is used to capture the text descriptions of a BPMN element
     */
    @XmlAttribute
    private String text;

    /**
     * This attribute identifies the format of the text. It MUST follow the mime-type format. The
     * default is "text/plain."
     */
    @XmlAttribute
    private String textFormat = "text/plain";

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Documentation [text=%s, textFormat=%s, id=%s, documentation=%s, extensionElements=%s]",
                text, textFormat, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextFormat() {
        return textFormat;
    }

    public void setTextFormat(String textFormat) {
        this.textFormat = textFormat;
    }
}
