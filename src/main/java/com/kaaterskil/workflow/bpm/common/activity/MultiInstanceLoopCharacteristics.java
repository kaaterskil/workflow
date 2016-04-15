package com.kaaterskil.workflow.bpm.common.activity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.common.Expression;
import com.kaaterskil.workflow.bpm.common.data.DataInput;
import com.kaaterskil.workflow.bpm.common.data.DataOutput;

/**
 * The MultiInstanceLoopCharacteristics class allows for creation of a desired number of Activity
 * instances. The instances MAY execute in parallel or MAY be sequential. Either an Expression is
 * used to specify or calculate the desired number of instances or a data driven setup can be used.
 * In that case a data input can be specified, which is able to handle a collection of data. The
 * number of items in the collection determines the number of Activity instances. This data input
 * can be produced by an input Data Association. The modeler can also configure this loop to control
 * the tokens produced.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MultiInstanceLoopCharacteristics extends LoopCharacteristics {

    /**
     * This attribute is a flag that controls whether the Activity instances will execute
     * sequentially or in parallel.
     */
    @XmlAttribute
    private boolean isSequential = false;

    /**
     * A numeric Expression that controls the number of Activity instances that will be created.
     * This Expression MUST evaluate to an integer. This MAY be under-specified, meaning that the
     * modeler MAY simply document the condition. In such a case the loop cannot be formally
     * executed. In order to initialize a valid multi-instance, either the loopCardinality
     * Expression or the loopDataInput MUST be specified.
     */
    @XmlElement(name = "loopCardinality", type = Expression.class, required = false)
    private Expression loopCardinality;

    /**
     * This ItemAwareElement is used to determine the number of Activity instances, one Activity
     * instance per item in the collection of data stored in that ItemAwareElement element. For
     * Tasks it is a reference to a Data Input which is part of the Activity’s
     * InputOutputSpecification. For Sub-Processes it is a reference to a collection-valued Data
     * Object in the context that is visible to the Sub-Processes. In order to initialize a valid
     * multi-instance, either the loopCardinality Expression or the loopDataInput MUST be specified.
     */
    @XmlElement(required = false)
    private String loopDataInputRef;

    /**
     * This ItemAwareElement specifies the collection of data, which will be produced by the
     * multi-instance. For Tasks it is a reference to a Data Output which is part of the Activity’s
     * InputOutputSpecification. For Sub-Processes it is a reference to a collection-valued Data
     * Object in the context that is visible to the Sub-Processes.
     */
    @XmlElement(required = false)
    private String loopDataOutputRef;

    /**
     * A Data Input, representing for every Activity instance the single item of the collection
     * stored in the loopDataInput. This Data Input can be the source of DataInputAssociation to a
     * data input of the Activity’s InputOutputSpecification. The type of this Data Input MUST the
     * scalar of the type defined for the loopDataInput.
     */
    @XmlElement(name = "inputDataItem", type = DataInput.class, required = false)
    private DataInput inputDataItem;

    /**
     * A Data Output, representing for every Activity instance the single item of the collection
     * stored in the loopDataOutput. This Data Output can be the target of DataOutputAssociation to
     * a data output of the Activity’s InputOutputSpecification. The type of this Data Output MUST
     * the scalar of the type defined for the loopDataOutput.
     */
    @XmlElement(name = "outputDataItem", type = DataOutput.class, required = false)
    private DataOutput outputDataItem;

    /**
     * The attribute behavior acts as a shortcut for specifying when events SHALL be thrown from an
     * Activity instance that is about to complete. It can assume values of None, One, All, and
     * Complex, resulting in the following behavior:
     * <ul>
     * <li>None: the EventDefinition which is associated through the noneEvent association will be
     * thrown for each instance completing.
     * <li>One: the EventDefinition referenced through the oneEvent association will be thrown upon
     * the first instance completing.
     * <li>All: no Event is ever thrown; a token is produced after completion of all instances.
     * <li>Complex: the complexBehaviorDefinitions are consulted to determine if and which Events to
     * throw.
     * </ul>
     * <p>
     * For the behaviors of none and one, a default SignalEventDefinition will be thrown which
     * automatically carries the current runtime attributes of the MI Activity. Any thrown Events
     * can be caught by boundary Events on the Multi-Instance Activity.
     */
    @XmlElement(name = "behavior", type = MultiInstanceFlowCondition.class)
    private MultiInstanceFlowCondition behavior = MultiInstanceFlowCondition.ALL;

    /**
     * Controls when and which Events are thrown in case behavior is set to complex.
     */
    @XmlElement(name = "complexBehaviorDefinition", type = ComplexBehaviorDefinition.class, required = false)
    private List<ComplexBehaviorDefinition> complexBehaviorDefinition = new ArrayList<>();

    /**
     * This attribute defines a boolean Expression that when evaluated to true, cancels the
     * remaining Activity instances and produces a token.
     */
    @XmlElement(name = "completionCondition", type = Expression.class, required = false)
    private Expression completionCondition;

    /**
     * The EventDefinition which is thrown when behavior is set to one and the first internal
     * Activity instance has completed.
     */
    @XmlAttribute
    private String oneBehaviorEventRef;

    /**
     * The EventDefinition which is thrown when the behavior is set to none and an internal Activity
     * instance has completed.
     */
    @XmlAttribute
    private String noneBehaviorEventRef;

    /*---------- Instance properties ----------*/

    /**
     * This attribute is provided for each generated (inner) instance of the Activity. It contains
     * the sequence number of the generated instance, i.e., if this value of some instance in n, the
     * instance is the nth instance that was generated.
     */
    @XmlTransient
    private int loopCounter;

    /**
     * This attribute is provided for the outer instance of the Multi-Instance Activity only. This
     * attribute contains the total number of inner instances created for the Multi-Instance
     * Activity.
     */
    @XmlTransient
    private int numberOfInstances;

    /**
     * This attribute is provided for the outer instance of the Multi-Instance Activity only. This
     * attribute contains the number of currently active inner instances for the Multi-Instance
     * Activity. In case of a sequential Multi-Instance Activity, this value can’t be greater than
     * 1. For parallel Multi-Instance Activities, this value can’t be greater than the value
     * contained in numberOfInstances.
     */
    @XmlTransient
    private int numberOfActiveInstances;

    /**
     * This attribute is provided for the outer instance of the Multi-Instance Activity only. This
     * attribute contains the number of already completed inner instances for the Multi-Instance
     * Activity.
     */
    @XmlTransient
    private int numberOfCompletedInstances;

    /**
     * This attribute is provided for the outer instance of the Multi-Instance Activity only. This
     * attribute contains the number of terminated inner instances for the Multi-Instance Activity.
     * The sum of numberOfTerminatedInstances, numberOfCompletedInstances, and
     * numberOfActiveInstances always sums up to numberOfInstances.
     */
    @XmlTransient
    private int numberOfTerminatedInstances;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "MultiInstanceLoopCharacteristics [loopCardinality=%s, loopDataInputRef=%s, loopDataOutputRef=%s, inputDataItem=%s, outputDataItem=%s, complexBehaviorDefinition=%s, completionCondition=%s, isSequential=%s, behavior=%s, oneBehaviorEventRef=%s, noneBehaviorEventRef=%s, loopCounter=%s, numberOfInstances=%s, numberOfActiveInstances=%s, numberOfCompletedInstances=%s, numberOfTerminatedInstances=%s, id=%s, documentation=%s, extensionElements=%s]",
                loopCardinality, loopDataInputRef, loopDataOutputRef, inputDataItem, outputDataItem,
                complexBehaviorDefinition, completionCondition, isSequential, behavior,
                oneBehaviorEventRef, noneBehaviorEventRef, loopCounter, numberOfInstances,
                numberOfActiveInstances, numberOfCompletedInstances, numberOfTerminatedInstances,
                id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public Expression getLoopCardinality() {
        return loopCardinality;
    }

    public void setLoopCardinality(Expression loopCardinality) {
        this.loopCardinality = loopCardinality;
    }

    public String getLoopDataInputRef() {
        return loopDataInputRef;
    }

    public void setLoopDataInputRef(String loopDataInputRef) {
        this.loopDataInputRef = loopDataInputRef;
    }

    public String getLoopDataOutputRef() {
        return loopDataOutputRef;
    }

    public void setLoopDataOutputRef(String loopDataOutputRef) {
        this.loopDataOutputRef = loopDataOutputRef;
    }

    public DataInput getInputDataItem() {
        return inputDataItem;
    }

    public void setInputDataItem(DataInput inputDataItem) {
        this.inputDataItem = inputDataItem;
    }

    public DataOutput getOutputDataItem() {
        return outputDataItem;
    }

    public void setOutputDataItem(DataOutput outputDataItem) {
        this.outputDataItem = outputDataItem;
    }

    public List<ComplexBehaviorDefinition> getComplexBehaviorDefinition() {
        return complexBehaviorDefinition;
    }

    public void setComplexBehaviorDefinition(
            List<ComplexBehaviorDefinition> complexBehaviorDefinition) {
        this.complexBehaviorDefinition = complexBehaviorDefinition;
    }

    public Expression getCompletionCondition() {
        return completionCondition;
    }

    public void setCompletionCondition(Expression completionCondition) {
        this.completionCondition = completionCondition;
    }

    public boolean isSequential() {
        return isSequential;
    }

    public void setSequential(boolean isSequential) {
        this.isSequential = isSequential;
    }

    public MultiInstanceFlowCondition getBehavior() {
        return behavior;
    }

    public void setBehavior(MultiInstanceFlowCondition behavior) {
        this.behavior = behavior;
    }

    public String getOneBehaviorEventRef() {
        return oneBehaviorEventRef;
    }

    public void setOneBehaviorEventRef(String oneBehaviorEventRef) {
        this.oneBehaviorEventRef = oneBehaviorEventRef;
    }

    public String getNoneBehaviorEventRef() {
        return noneBehaviorEventRef;
    }

    public void setNoneBehaviorEventRef(String noneBehaviorEventRef) {
        this.noneBehaviorEventRef = noneBehaviorEventRef;
    }

    @Override
    public int getLoopCounter() {
        return loopCounter;
    }

    @Override
    public void setLoopCounter(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    public int getNumberOfInstances() {
        return numberOfInstances;
    }

    public void setNumberOfInstances(int numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
    }

    public int getNumberOfActiveInstances() {
        return numberOfActiveInstances;
    }

    public void setNumberOfActiveInstances(int numberOfActiveInstances) {
        this.numberOfActiveInstances = numberOfActiveInstances;
    }

    public int getNumberOfCompletedInstances() {
        return numberOfCompletedInstances;
    }

    public void setNumberOfCompletedInstances(int numberOfCompletedInstances) {
        this.numberOfCompletedInstances = numberOfCompletedInstances;
    }

    public int getNumberOfTerminatedInstances() {
        return numberOfTerminatedInstances;
    }

    public void setNumberOfTerminatedInstances(int numberOfTerminatedInstances) {
        this.numberOfTerminatedInstances = numberOfTerminatedInstances;
    }
}
