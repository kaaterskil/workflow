package com.kaaterskil.workflow.bpm.common.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

/**
 * Properties, like Data Objects, are item-aware elements. But, unlike Data Objects, they are not
 * visually displayed on a Process diagram. Certain flow elements MAY contain properties, in
 * particular only Processes, Activities, and Events MAY contain Properties. The Property class is a
 * DataElement element that acts as a container for data associated with flow elements. Property
 * elements MUST be contained within a FlowElement. Property elements are not visually displayed on
 * a Process diagram.
 * <p>
 * The lifecycle of a Property is tied to the lifecycle of its parent Flow Element. When a Flow
 * Element is instantiated, all Properties contained by it are also instantiated. When a Flow
 * Element instance is disposed, all Property instances contained by it are also disposed. At this
 * point the data within these instances are no longer available.
 * <p>
 * The accessibility of a Property is driven by its lifecycle. The data within a Property can only
 * be accessed when there is guaranteed to be a live Property instance present. As a result, a
 * Property can only be accessed by its parent Process, Sub-Process, or Flow Element. In case the
 * parent is a Process or Sub-Process, then a property can be accessed by the immediate children
 * (including children elements) of that Process or Sub-Process.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Property extends BaseElement implements ItemAwareElement {

    /**
     * Defines the name of the property
     */
    @XmlAttribute
    private String name;

    /**
     * Specification of the items that are stored or conveyed by the ItemAwareElement. Basically,
     * this is a reference to an ItemDefinition.
     */
    @XmlAttribute
    private String itemSubjectRef;

    /**
     * A reference to the DataState, which defines certain states for the data contained in the
     * Item.
     */
    @XmlTransient
    private DataState dataState;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "Property [name=%s, itemSubjectRef=%s, dataState=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, itemSubjectRef, dataState, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getItemSubjectRef() {
        return itemSubjectRef;
    }

    @Override
    public void setItemSubjectRef(String itemSubjectRef) {
        this.itemSubjectRef = itemSubjectRef;
    }

    @Override
    public DataState getDataState() {
        return dataState;
    }

    @Override
    public void setDataState(DataState dataState) {
        this.dataState = dataState;
    }

}
