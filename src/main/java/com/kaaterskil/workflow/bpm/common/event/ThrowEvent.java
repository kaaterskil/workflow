package com.kaaterskil.workflow.bpm.common.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.data.DataInput;
import com.kaaterskil.workflow.bpm.common.data.DataInputAssociation;
import com.kaaterskil.workflow.bpm.common.data.InputSet;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ThrowEvent extends Event {

    @XmlAttribute
    protected List<String> eventDefinitionRefs = new ArrayList<>();

    @XmlElementWrapper(name = "eventDefinitions")
    @XmlElements({
            @XmlElement(name = "cancelEventDefinition", type = CancelEventDefinition.class),
            @XmlElement(name = "compensateEventDefinition", type = CompensateEventDefinition.class),
            @XmlElement(name = "conditionalEventDefinition",
                        type = ConditionalEventDefinition.class),
            @XmlElement(name = "errorEventDefinition", type = ErrorEventDefinition.class),
            @XmlElement(name = "escalationEventDefinition", type = EscalationEventDefinition.class),
            @XmlElement(name = "linkEventDefinition", type = LinkEventDefinition.class),
            @XmlElement(name = "messageEventDefinition", type = MessageEventDefinition.class),
            @XmlElement(name = "signalEventDefinition", type = SignalEventDefinition.class),
            @XmlElement(name = "terminateEventDefinition", type = TerminateEventDefinition.class),
            @XmlElement(name = "timerEventDefinition", type = TimerEventDefinition.class) })
    protected List<EventDefinition> eventDefinitions = new ArrayList<>();

    @XmlElementWrapper(name = "dataInputAssociations")
    @XmlElement(name = "dataInputAssociation", type = DataInputAssociation.class)
    protected List<DataInputAssociation> dataInputAssociations = new ArrayList<>();

    @XmlElementWrapper(name = "dataInputs")
    @XmlElement(name = "dataInput", type = DataInput.class)
    protected List<DataInput> dataInputs = new ArrayList<>();

    @XmlElement(name = "inputSet", type = InputSet.class)
    protected InputSet inputSet;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "ThrowEvent [eventDefinitionRefs=%s, eventDefinitions=%s, dataInputAssociations=%s, dataInputs=%s, inputSet=%s, properties=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                eventDefinitionRefs, eventDefinitions, dataInputAssociations, dataInputs, inputSet,
                properties, incoming, outgoing, name, categoryValueRef, id, documentation,
                extensionElements);
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

    public List<DataInputAssociation> getDataInputAssociations() {
        return dataInputAssociations;
    }

    public void setDataInputAssociations(List<DataInputAssociation> dataInputAssociations) {
        this.dataInputAssociations = dataInputAssociations;
    }

    public List<DataInput> getDataInputs() {
        return dataInputs;
    }

    public void setDataInputs(List<DataInput> dataInputs) {
        this.dataInputs = dataInputs;
    }

    public InputSet getInputSet() {
        return inputSet;
    }

    public void setInputSet(InputSet inputSet) {
        this.inputSet = inputSet;
    }

}
