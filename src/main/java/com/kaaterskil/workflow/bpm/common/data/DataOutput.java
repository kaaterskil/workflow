package com.kaaterskil.workflow.bpm.common.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class DataOutput extends BaseElement implements ItemAwareElement {

    @XmlAttribute(required = false)
    private String name;

    @XmlAttribute
    private boolean isCollection = false;

    @XmlAttribute
    private String itemSubjectRef;

    @XmlElement
    private List<String> outputSetRefs = new ArrayList<>();

    @XmlTransient
    private DataState dataState;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "DataOutput [name=%s, isCollection=%s, itemSubjectRef=%s, dataState=%s, outputSetRefs=%s, id=%s, documentation=%s, extensionElements=%s]",
                name, isCollection, itemSubjectRef, dataState, outputSetRefs, id, documentation,
                extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean isCollection) {
        this.isCollection = isCollection;
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

    public List<String> getOutputSetRefs() {
        return outputSetRefs;
    }

    public void setOutputSetRefs(List<String> outputSetRefs) {
        this.outputSetRefs = outputSetRefs;
    }

}
