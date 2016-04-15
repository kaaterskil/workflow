package com.kaaterskil.workflow.bpm.common.artifact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Group object is an Artifact that provides a visual mechanism to group elements of a diagram
 * informally. The grouping is tied to the CategoryValue supporting element. That is, a Group is a
 * visual depiction of a single CategoryValue. The graphical elements within the Group will be
 * assigned the CategoryValue of the Group. (Note -- CategoryValues can be highlighted through other
 * mechanisms, such as color, as defined by a modeler or a modeling tool).
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Group extends Artifact {

    /**
     * The categoryValueRef attribute specifies the CategoryValue that the Group represents.
     * (Further details about the definition of a Category and CategoryValue can be found on page
     * 70.) The name of the Category and the value of the CategoryValue separated by delineator "."
     * provides the label for the Group. The graphical elements within the boundaries of the Group
     * will be assigned the CategoryValue.
     */
    @XmlAttribute
    private String categoryValueRef;

    @Override
    public String toString() {
        return String.format(
                "Group [categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                categoryValueRef, id, documentation, extensionElements);
    }

    public String getCategoryValueRef() {
        return categoryValueRef;
    }

    public void setCategoryValueRef(String categoryValueRef) {
        this.categoryValueRef = categoryValueRef;
    }
}
