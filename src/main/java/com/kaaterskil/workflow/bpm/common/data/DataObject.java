package com.kaaterskil.workflow.bpm.common.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.FlowElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class DataObject extends FlowElement implements ItemAwareElement {

    @XmlAttribute
    private String itemSubjectRef;

    @XmlAttribute
    private boolean isCollection;

    @XmlElement(name = "dataState", type = DataState.class, required = false)
    private DataState dataState;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "DataObject [itemSubjectRef=%s, dataState=%s, isCollection=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                itemSubjectRef, dataState, isCollection, name, categoryValueRef, id, documentation,
                extensionElements);
    }

    /*---------- Getter/Setters ----------*/

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

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

}
