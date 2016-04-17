package com.kaaterskil.workflow.engine.parser.factory;

import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.engine.bpm.ClassDelegateFactory;
import com.kaaterskil.workflow.engine.bpm.DelegateWorkflowEventListener;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;
import com.kaaterskil.workflow.engine.persistence.entity.CommentEntity;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessInstanceEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class ListenerFactoryImpl extends AbstractBehaviorFactory implements ListenerFactory {

    private final ClassDelegateFactory classDelegateFactory;

    public ListenerFactoryImpl(ClassDelegateFactory classDelegateFactory) {
        this.classDelegateFactory = classDelegateFactory;
    }

    public ListenerFactoryImpl() {
        this(new ClassDelegateFactory());
    }

    @Override
    public TokenListener createClassDelegateTokenListener(Listener listener) {
        return classDelegateFactory.create(listener.getImplementation(), createFieldDeclarations(listener.getExtensionAttributes()));
    }

    @Override
    public WorkflowEventListener createClassDelegateEventListener(Listener listener) {
        return new DelegateWorkflowEventListener(listener.getImplementation(), getEntityClazz(listener.getEntityType()));
    }

    private Class<?> getEntityClazz(String entityType) {
        if (entityType != null) {
            switch (entityType) {
            case "comment":
                return CommentEntity.class;
            case "token":
                return Token.class;
            case "job":
                return JobEntity.class;
            case "process-definition":
                return ProcessDefinitionEntity.class;
            case "process-instance":
                return ProcessInstanceEntity.class;
            default:
                throw new IllegalArgumentException(
                        "Unsupported entity type for Workflow Event Listener: " + entityType);
            }
        }
        return null;
    }
}
