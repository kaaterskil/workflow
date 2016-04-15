package com.kaaterskil.workflow.bpm.common.artifact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Text Annotations are a mechanism for a modeler to provide additional information for the reader
 * of a BPMN Diagram.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TextAnnotation extends Artifact {

    /**
     * Text is an attribute that is text that the modeler wishes to communicate to the reader of the
     * Diagram.
     */
    @XmlAttribute
    private String text;

    /**
     * This attribute identifies the format of the text. It MUST follow the mime- type format. The
     * default is "text/plain."
     */
    @XmlAttribute
    private String textFormat = "text/plain";

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format("TextAnnotation [text=%s, textFormat=%s]", text, textFormat);
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
