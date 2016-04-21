package com.kaaterskil.workflow.engine.parser.factory;

import com.kaaterskil.workflow.bpm.common.activity.ServiceTask;
import com.kaaterskil.workflow.bpm.common.event.StartEvent;
import com.kaaterskil.workflow.engine.behavior.NoneStartEventActivityBehavior;
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

    /*---------- Getter/Setters ----------*/

    public ClassDelegateFactory getClassDelegateFactory() {
        return classDelegateFactory;
    }

    public void setClassDelegateFactory(ClassDelegateFactory classDelegateFactory) {
        this.classDelegateFactory = classDelegateFactory;
    }
}
