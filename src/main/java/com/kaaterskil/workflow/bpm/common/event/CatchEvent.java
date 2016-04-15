package com.kaaterskil.workflow.bpm.common.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.data.DataOutput;
import com.kaaterskil.workflow.bpm.common.data.DataOutputAssociation;
import com.kaaterskil.workflow.bpm.common.data.OutputSet;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class CatchEvent extends Event {

    @XmlElement
    protected List<String> eventDefinitionRefs = new ArrayList<>();

    @XmlElementWrapper(name = "eventDefinitions")
    @XmlElement(name = "eventDefinition", type = EventDefinition.class)
    protected List<EventDefinition> eventDefinitions = new ArrayList<>();

    @XmlElementWrapper(name = "dataOutputAssociations")
    @XmlElement(name = "dataOutputAssociation", type = DataOutputAssociation.class)
    protected List<DataOutputAssociation> dataOutputAssociations = new ArrayList<>();

    @XmlElementWrapper(name = "dataOutputs")
    @XmlElement(name = "dataOutput", type = DataOutput.class)
    protected List<DataOutput> dataOutputs = new ArrayList<>();

    @XmlElement
    protected OutputSet outputSet;

    @XmlAttribute
    protected boolean parallelMultiple = false;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "CatchEvent [eventDefinitionRefs=%s, eventDefinitions=%s, dataOutputAssociations=%s, dataOutputs=%s, outputSet=%s, parallelMultiple=%s, properties=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                eventDefinitionRefs, eventDefinitions, dataOutputAssociations, dataOutputs,
                outputSet, parallelMultiple, properties, incoming, outgoing, name, categoryValueRef,
                id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public List<String> getEventDefinitionRefs() {
        return eventDefinitionRefs;
    }

    public void setEventDefinitionRefs(List<String> eventDefinitionRefs) {
        this.eventDefinitionRefs = eventDefinitionRefs;
    }

    public List<EventDefinition> getEventDefinitions() {
        return eventDefinitions;
    }

    public void setEventDefinitions(List<EventDefinition> eventDefinitions) {
        this.eventDefinitions = eventDefinitions;
    }

    public List<DataOutputAssociation> getDataOutputAssociations() {
        return dataOutputAssociations;
    }

    public void setDataOutputAssociations(List<DataOutputAssociation> dataOutputAssociations) {
        this.dataOutputAssociations = dataOutputAssociations;
    }

    public List<DataOutput> getDataOutputs() {
        return dataOutputs;
    }

    public void setDataOutputs(List<DataOutput> dataOutputs) {
        this.dataOutputs = dataOutputs;
    }

    public OutputSet getOutputSet() {
        return outputSet;
    }

    public void setOutputSet(OutputSet outputSet) {
        this.outputSet = outputSet;
    }

    public boolean isParallelMultiple() {
        return parallelMultiple;
    }

    public void setParallelMultiple(boolean parallelMultiple) {
        this.parallelMultiple = parallelMultiple;
    }
}
