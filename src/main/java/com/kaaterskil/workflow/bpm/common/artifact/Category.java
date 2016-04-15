package com.kaaterskil.workflow.bpm.common.artifact;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.RootElement;

/**
 * Categories, which have user-defined semantics, can be used for documentation or analysis
 * purposes. For example, FlowElements can be categorized as being customer oriented vs. support
 * oriented. Furthermore, the cost and time of Activities per Category can be calculated.
 * <p>
 * Groups are one way in which Categories of objects can be visually displayed on the diagram. That
 * is, a Group is a visual depiction of a single CategoryValue. The graphical elements within the
 * Group will be assigned the CategoryValue of the Group. The value of the CategoryValue, optionally
 * prepended by the Category name and delineator ":", appears on the diagram as the Group label.
 * (Note -- Categories can be highlighted through other mechanisms, such as color, as defined by a
 * modeler or a modeling tool). A single Category can be used for multiple Groups in a diagram.
 * </p>
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Category extends RootElement {

    /**
     * The descriptive name of the element.
     */
    @XmlAttribute
    private String name;

    /**
     * The categoryValue attribute specifies one or more values of the Category. For example, the
     * Category is “Region” then this Category could specify values like “North,” “South,” “West,”
     * and “East.”
     */
    @XmlElementWrapper(name = "categoryValues")
    @XmlElement(name = "categoryValue", type = CategoryValue.class)
    private List<CategoryValue> categoryValues = new ArrayList<>();

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Category [name=%s, categoryValues=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, categoryValues, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryValue> getCategoryValues() {
        return categoryValues;
    }

    public void setCategoryValues(List<CategoryValue> categoryValues) {
        this.categoryValues = categoryValues;
    }
}
