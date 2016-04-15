package com.kaaterskil.workflow.bpm.common.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.common.FlowElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class DataStore extends FlowElement implements ItemAwareElement {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private int capacity;

    @XmlAttribute
    private boolean isUnlimited = false;

    @XmlAttribute
    private String itemSubjectRef;

    @XmlTransient
    private DataState dataState;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "DataStore [name=%s, capacity=%s, isUnlimited=%s, itemSubjectRef=%s, dataState=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, capacity, isUnlimited, itemSubjectRef, dataState, categoryValueRef, id,
                documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isUnlimited() {
        return isUnlimited;
    }

    public void setUnlimited(boolean isUnlimited) {
        this.isUnlimited = isUnlimited;
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
