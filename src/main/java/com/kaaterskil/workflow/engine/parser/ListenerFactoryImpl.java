package com.kaaterskil.workflow.engine.parser;

import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.engine.bpm.ClassDelegateFactory;
import com.kaaterskil.workflow.engine.delegate.TokenListener;

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
        return classDelegateFactory.create(listener.getImplementation(),
                createFieldDeclarations(listener.getExtensionElements().values()));
    }
}
