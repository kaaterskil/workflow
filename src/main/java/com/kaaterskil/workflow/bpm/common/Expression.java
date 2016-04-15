package com.kaaterskil.workflow.bpm.common;

import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

/**
 * The Expression class is used to specify an Expression using natural-language text. These
 * Expressions are not executable. The natural language text is captured using the documentation
 * attribute, inherited from BaseElement.
 *
 * @author bcaple
 */
@XmlRootElement
public class Expression extends BaseElement {

    @Override
    public String toString() {
        return String.format("Expression [id=%s, documentation=%s, extensionElements=%s]", id,
                documentation, extensionElements);
    }

}
