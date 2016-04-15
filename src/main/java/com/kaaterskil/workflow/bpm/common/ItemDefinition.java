package com.kaaterskil.workflow.bpm.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.RootElement;

/**
 * BPMN elements, such as DataObjects and Messages, represent items that are manipulated,
 * transferred, transformed, or stored during Process flows. These items can be either physical
 * items, such as the mechanical part of a vehicle, or information items such the catalog of the
 * mechanical parts of a vehicle.
 * <p>
 * An important characteristics of items in Process is their structure. BPMN does not require a
 * particular format for this data structure, but it does designate XML Schema as its default. The
 * structure attribute references the actual data structure.
 * <p>
 * The default format of the data structure for all elements can be specified in the Definitions
 * element using the typeLanguage attribute. For example, a typeLanguage value of
 * http://www.w3.org/2001/XMLSchema” indicates that the data structures using by elements within
 * that Definitions are in the form of XML Schema types. If unspecified, the default is XML schema.
 * An Import is used to further identify the location of the data structure (if applicable). For
 * example, in the case of data structures contributed by an XML schema, an Import would be used to
 * specify the file location of that schema.
 * <p>
 * Structure definitions are always defined as separate entities, so they cannot be inlined in one
 * of their usages. You will see that in every mention of structure definition there is a
 * “reference” to the element. This is why this class inherits from RootElement.
 * <p>
 * An ItemDefinition element can specify an import reference where the proper definition of the
 * structure is defined.
 * </p>
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ItemDefinition extends RootElement {

    /**
     * This defines the nature of the Item. Possible values are physical or information. The default
     * value is information.
     */
    @XmlAttribute
    private ItemKind itemKind = ItemKind.INFORMATION;

    /**
     * The concrete data structure to be used.
     */
    @XmlAttribute
    private String structureRef;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "ItemDefinition [itemKind=%s, structureRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                itemKind, structureRef, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public ItemKind getItemKind() {
        return itemKind;
    }

    public void setItemKind(ItemKind itemKind) {
        this.itemKind = itemKind;
    }

    public String getStructureRef() {
        return structureRef;
    }

    public void setStructureRef(String structureRef) {
        this.structureRef = structureRef;
    }
}
