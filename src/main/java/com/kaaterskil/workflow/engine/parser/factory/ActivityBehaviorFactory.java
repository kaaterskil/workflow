package com.kaaterskil.workflow.engine.parser.factory;

import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.ServiceTask;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.activity.Task;
import com.kaaterskil.workflow.bpm.common.activity.Transaction;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.event.CompensateEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.EndEvent;
import com.kaaterskil.workflow.bpm.common.event.ErrorEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.IntermediateCatchEvent;
import com.kaaterskil.workflow.bpm.common.event.StartEvent;
import com.kaaterskil.workflow.bpm.common.gateway.EventBasedGateway;
import com.kaaterskil.workflow.bpm.common.gateway.ExclusiveGateway;
import com.kaaterskil.workflow.bpm.common.gateway.InclusiveGateway;
import com.kaaterskil.workflow.bpm.common.gateway.ParallelGateway;
import com.kaaterskil.workflow.engine.behavior.BaseActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.BoundaryCompensateEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.BoundaryEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.CancelEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.ErrorEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.EventBasedGatewayActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.ExclusiveGatewayActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.InclusiveGatewayActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.IntermediateCatchEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.NoneEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.NoneStartEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.ParallelGatewayActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.ParallelMultiInstanceBehavior;
import com.kaaterskil.workflow.engine.behavior.SequentialMultiInstanceBehavior;
import com.kaaterskil.workflow.engine.behavior.SubProcessActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.TaskActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.TerminateEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.TransactionActivityBehavior;
import com.kaaterskil.workflow.engine.bpm.ClassDelegate;
import com.kaaterskil.workflow.engine.bpm.ClassDelegateFactory;
import com.kaaterskil.workflow.engine.parser.BpmModel;

public class ActivityBehaviorFactory extends AbstractBehaviorFactory {

    private ClassDelegateFactory classDelegateFactory;
    private BpmModel bpmModel;

    public ActivityBehaviorFactory() {
        classDelegateFactory = new ClassDelegateFactory();
    }

    /*---------- Start Event ----------*/

    public NoneStartEventActivityBehavior createNoneStartEventActivityBehavior(
            StartEvent startEvent) {
        return new NoneStartEventActivityBehavior();
    }

    /*---------- Tasks ----------*/

    public TaskActivityBehavior createTaskActivityBehavior(Task task) {
        return new TaskActivityBehavior();
    }

    public ClassDelegate createClassDelegateServiceTask(ServiceTask task) {
        return classDelegateFactory.create(task.getImplementation(),
                createFieldDeclarations(extractFieldExtensions(task)));
    }

    /*---------- Gateways ----------*/

    public ExclusiveGatewayActivityBehavior createExclusiveGatewayActivityBehavior(
            ExclusiveGateway exclusiveGateway) {
        return new ExclusiveGatewayActivityBehavior();
    }

    public InclusiveGatewayActivityBehavior createInclusiveGatewayActivityBehavior(
            InclusiveGateway inclusiveGateway) {
        return new InclusiveGatewayActivityBehavior();
    }

    public ParallelGatewayActivityBehavior createParallelGatewayActivityBehavior(
            ParallelGateway parallelGateway) {
        return new ParallelGatewayActivityBehavior();
    }

    public EventBasedGatewayActivityBehavior createEventBasedGatewayActivityBehavior(
            EventBasedGateway eventGateway) {
        return new EventBasedGatewayActivityBehavior();
    }

    /*---------- Multi-Instance ----------*/

    public SequentialMultiInstanceBehavior createSequentialMultiInstanceBehavior(Activity activity,
            BaseActivityBehavior innerActivityBehavior) {
        return new SequentialMultiInstanceBehavior(activity, innerActivityBehavior);
    }

    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(Activity activity,
            BaseActivityBehavior innerActivityBehavior) {
        return new ParallelMultiInstanceBehavior(activity, innerActivityBehavior);
    }

    /*---------- SubProcess ----------*/

    public SubProcessActivityBehavior createSubProcessActivityBehavior(SubProcess subProcess) {
        return new SubProcessActivityBehavior();
    }

    public TransactionActivityBehavior createTransactionActivityBehavior(Transaction transaction) {
        return new TransactionActivityBehavior();
    }

    /*---------- Intermediate Events ----------*/

    public IntermediateCatchEventActivityBehavior createIntermediateCatchEventActivityBehavior(
            IntermediateCatchEvent event) {
        return new IntermediateCatchEventActivityBehavior();
    }

    /*---------- End Events ----------*/

    public NoneEndEventActivityBehavior createNoneEndEventActivityBehavior(EndEvent endEvent) {
        return new NoneEndEventActivityBehavior();
    }

    public ErrorEndEventActivityBehavior createErrorEndEventActivityBehavior(EndEvent endEvent,
            ErrorEventDefinition errorEventDefinition) {
        return new ErrorEndEventActivityBehavior(errorEventDefinition.getErrorRef());
    }

    public CancelEndEventActivityBehavior createCancelEndEventActivityBehavior(EndEvent endEvent) {
        return new CancelEndEventActivityBehavior();
    }

    public TerminateEndEventActivityBehavior createTerminateEndEventActivityBehavior(
            EndEvent endEvent) {
        return new TerminateEndEventActivityBehavior();
    }

    /*---------- Boundary Events ----------*/

    public BoundaryEventActivityBehavior createBoundaryEventActivityBehavior(
            BoundaryEvent boundaryEvent, boolean isInterrupting) {
        return new BoundaryEventActivityBehavior(isInterrupting);
    }

    public BoundaryCompensateEventActivityBehavior createBoundaryCompensateEventActivityBehavior(
            BoundaryEvent boundaryEvent, CompensateEventDefinition compensateEventDefinition,
            boolean isInterrupting) {
        return new BoundaryCompensateEventActivityBehavior(isInterrupting,
                compensateEventDefinition);
    }

    /*---------- Getter/Setters ----------*/

    public ClassDelegateFactory getClassDelegateFactory() {
        return classDelegateFactory;
    }

    public void setClassDelegateFactory(ClassDelegateFactory classDelegateFactory) {
        this.classDelegateFactory = classDelegateFactory;
    }

    public BpmModel getBpmModel() {
        return bpmModel;
    }

    public void setBpmModel(BpmModel bpmModel) {
        this.bpmModel = bpmModel;
    }
}
