package com.kaaterskil.workflow.engine.parser.factory;

import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.ServiceTask;
import com.kaaterskil.workflow.bpm.common.event.EndEvent;
import com.kaaterskil.workflow.bpm.common.event.ErrorEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.StartEvent;
import com.kaaterskil.workflow.engine.behavior.BaseActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.CancelEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.ErrorEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.NoneEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.NoneStartEventActivityBehavior;
import com.kaaterskil.workflow.engine.behavior.ParallelMultiInstanceBehavior;
import com.kaaterskil.workflow.engine.behavior.SequentialMultiInstanceBehavior;
import com.kaaterskil.workflow.engine.behavior.TerminateEndEventActivityBehavior;
import com.kaaterskil.workflow.engine.bpm.ClassDelegate;
import com.kaaterskil.workflow.engine.bpm.ClassDelegateFactory;

public class ActivityBehaviorFactory extends AbstractBehaviorFactory {

    private ClassDelegateFactory classDelegateFactory;

    public ActivityBehaviorFactory() {
        classDelegateFactory = new ClassDelegateFactory();
    }

    public NoneStartEventActivityBehavior createNoneStartEventActivityBehavior(
            StartEvent startEvent) {
        return new NoneStartEventActivityBehavior();
    }

    public ClassDelegate createClassDelegateServiceTask(ServiceTask task) {
        return classDelegateFactory.create(task.getImplementation(),
                createFieldDeclarations(extractFieldExtensions(task)));
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

    /*---------- Getter/Setters ----------*/

    public ClassDelegateFactory getClassDelegateFactory() {
        return classDelegateFactory;
    }

    public void setClassDelegateFactory(ClassDelegateFactory classDelegateFactory) {
        this.classDelegateFactory = classDelegateFactory;
    }
}
