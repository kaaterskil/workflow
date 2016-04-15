package com.kaaterskil.workflow.bpm.common.activity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.data.DataInputAssociation;
import com.kaaterskil.workflow.bpm.common.data.DataOutputAssociation;
import com.kaaterskil.workflow.bpm.common.data.InputOutputSpecification;
import com.kaaterskil.workflow.bpm.common.data.Property;

/**
 * An Activity is work that is performed within a Business Process. An Activity can be atomic or
 * non-atomic (compound). The types of Activities that are a part of a Process are: Task,
 * Sub-Process, and Call Activity, which allows the inclusion of re-usable Tasks and Processes in
 * the diagram. However, a Process is not a specific graphical object. Instead, it is a set of
 * graphical objects. The following sections will focus on the graphical objects Sub-Process and
 * Task.
 * <p>
 * Activities represent points in a Process flow where work is performed. They are the executable
 * elements of a BPMN Process.
 * <p>
 * The Activity class is an abstract element, sub-classing from FlowElement (as shown in Figure
 * 10.6).
 * <p>
 * Sequence Flow Connections
 * <p>
 * See “Sequence Flow Connections Rules” on page 42 for the entire set of objects and how they MAY
 * be sources or targets of Sequence Flows.
 * <ul>
 * <li>An Activity MAY be a target for Sequence Flows; it can have multiple incoming Sequence Flows.
 * Incoming Sequence Flows MAY be from an alternative path and/or parallel paths. 􏰀 If the Activity
 * does not have an incoming Sequence Flow, then the Activity MUST be instantiated when the Process
 * is instantiated. 􏰀 There are two exceptions to this: Compensation Activities and Event
 * Sub-Processes. Note – If the Activity has multiple incoming Sequence Flows, then this is
 * considered uncontrolled flow. This means that when a token arrives from one of the Paths, the
 * Activity will be instantiated. It will not wait for the arrival of tokens from the other paths.
 * If another token arrives from the same path or another path, then a separate instance of the
 * Activity will be created. If the flow needs to be controlled, then the flow should converge on a
 * Gateway that precedes the Activities (see “Gateways” on page 287 for more information on
 * Gateways).
 * <li>An Activity MAY be a source for Sequence Flows; it can have multiple outgoing Sequence Flows.
 * If there are multiple outgoing Sequence Flows, then this means that a separate parallel path is
 * being created for each Sequence Flow (i.e., tokens will be generated for each outgoing Sequence
 * Flow from the Activity). 􏰀 If the Activity does not have an outgoing Sequence Flow, then the
 * Activity marks the end of one or more paths in the Process. When the Activity ends and there are
 * no other parallel paths active, then the Process MUST be completed. 􏰀 There are two exceptions
 * to this: Compensation Activities and Event Sub-Processes.
 * </ul>
 * <p>
 * Message Flow Connections
 * <p>
 * See “Message Flow Connection Rules” on page 43 for the entire set of objects and how they MAY be
 * sources or targets of Message Flows.
 * <p>
 * Note – All Message Flows MUST connect two separate Pools. They MAY connect to the Pool boundary
 * or to Flow Objects within the Pool boundary. They MUST NOT connect two objects within the same
 * Pool. 􏰀
 * <ul>
 * <li>An Activity MAY be the target of a Message Flow; it can have zero (0) or more incoming
 * Message Flows. 􏰀
 * <li>An Activity MAY be a source of a Message Flow; it can have zero (0) or more outgoing Message
 * Flows.
 * </ul>
 * </p>
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Activity extends FlowNode {

    /**
     * A flag that identifies whether this Activity is intended for the purposes of compensation. If
     * false, then this Activity executes as a result of normal execution flow. If true, this
     * Activity is only activated when a Compensation Event is detected and initiated under
     * Compensation Event visibility scope (see page 281 for more information on scopes).
     */
    @XmlAttribute
    protected boolean isForCompensation = false;

    /**
     * An Activity MAY be performed once or MAY be repeated. If repeated, the Activity MUST have
     * loopCharacteristics that define the repetition criteria (if the isExecutable attribute of the
     * Process is set to true).
     */
    @XmlElements({
            @XmlElement(name = "standardLoopCharacteristics",
                        type = StandardLoopCharacteristics.class, required = false),
            @XmlElement(name = "multiInstanceLoopCharacteristics",
                        type = MultiInstanceLoopCharacteristics.class, required = false) })
    protected LoopCharacteristics loopCharacteristics;

    /**
     * The Sequence Flow that will receive a token when none of the conditionExpressions on other
     * outgoing Sequence Flows evaluate to true. The default Sequence Flow should not have a
     * conditionExpression. Any such Expression SHALL be ignored.
     */
    @XmlAttribute(name = "default", required = false)
    protected String defaultSequenceFlow;

    /**
     * The InputOutputSpecification defines the inputs and outputs and the InputSets and OutputSets
     * for the Activity. See page 211 for more information on the InputOutputSpecification.
     */
    @XmlElement(name = "ioSpecification", type = InputOutputSpecification.class, required = false)
    protected InputOutputSpecification ioSpecification;

    /**
     * Modeler-defined properties MAY be added to an Activity. These properties are contained within
     * the Activity.
     */
    @XmlElement(name = "property", type = Property.class, required= false)
    protected List<Property> properties = new ArrayList<>();

    /**
     * This references the Intermediate Events that are attached to the boundary of the Activity.
     */
    @XmlElement(name = "boundaryEventRef", type = String.class, required = false)
    protected List<String> boundaryEventRefs = new ArrayList<>();

    /**
     * An optional reference to the DataInputAssociations. A DataInputAssociation defines how the
     * DataInput of the Activity’s InputOutputSpecification will be populated.
     */
    @XmlElement(name = "dataInputAssociation", type = DataInputAssociation.class, required = false)
    protected List<DataInputAssociation> dataInputAssociations = new ArrayList<>();

    /**
     * An optional reference to the DataOutputAssociations.
     */
    @XmlElement(name = "dataOutputAssociation", type = DataOutputAssociation.class, required = false)
    protected List<DataOutputAssociation> dataOutputAssociations = new ArrayList<>();

    /**
     * The default value is 1. The value MUST NOT be less than 1. This attribute defines the number
     * of tokens that MUST arrive before the Activity can begin. Note that any value for the
     * attribute that is greater than 1 is an advanced type of modeling and should be used with
     * caution.
     */
    @XmlAttribute
    protected int startQuantity = 1;

    /**
     * The default value is 1. The value MUST NOT be less than 1. This attribute defines the number
     * of tokens that MUST be generated from the Activity. This number of tokens will be sent done
     * any outgoing Sequence Flow (assuming any Sequence Flow conditions are satis- fied). Note that
     * any value for the attribute that is greater than 1 is an advanced type of modeling and should
     * be used with caution.
     */
    @XmlAttribute
    protected int completionQuantity = 1;

    @XmlTransient
    protected ActivityLifecycle state;

    /*---------- Getter/Setters ----------*/

    public boolean isForCompensation() {
        return isForCompensation;
    }

    public void setForCompensation(boolean isForCompensation) {
        this.isForCompensation = isForCompensation;
    }

    public LoopCharacteristics getLoopCharacteristics() {
        return loopCharacteristics;
    }

    public void setLoopCharacteristics(LoopCharacteristics loopCharacteristics) {
        this.loopCharacteristics = loopCharacteristics;
    }

    public String getDefaultSequenceFlow() {
        return defaultSequenceFlow;
    }

    public void setDefaultSequenceFlow(String defaultSequenceFlow) {
        this.defaultSequenceFlow = defaultSequenceFlow;
    }

    public InputOutputSpecification getIoSpecification() {
        return ioSpecification;
    }

    public void setIoSpecification(InputOutputSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<String> getBoundaryEventRefs() {
        return boundaryEventRefs;
    }

    public void setBoundaryEventRefs(List<String> boundaryEventRefs) {
        this.boundaryEventRefs = boundaryEventRefs;
    }

    public List<DataInputAssociation> getDataInputAssociations() {
        return dataInputAssociations;
    }

    public void setDataInputAssociations(List<DataInputAssociation> dataInputAssociations) {
        this.dataInputAssociations = dataInputAssociations;
    }

    public List<DataOutputAssociation> getDataOutputAssociations() {
        return dataOutputAssociations;
    }

    public void setDataOutputAssociations(List<DataOutputAssociation> dataOutputAssociations) {
        this.dataOutputAssociations = dataOutputAssociations;
    }

    public int getStartQuantity() {
        return startQuantity;
    }

    public void setStartQuantity(int startQuantity) {
        this.startQuantity = startQuantity;
    }

    public int getCompletionQuantity() {
        return completionQuantity;
    }

    public void setCompletionQuantity(int completionQuantity) {
        this.completionQuantity = completionQuantity;
    }

    public ActivityLifecycle getState() {
        return state;
    }

    public void setState(ActivityLifecycle state) {
        this.state = state;
    }
}
