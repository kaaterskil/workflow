package com.kaaterskil.workflow.engine.bpm;

import java.util.List;

import com.kaaterskil.workflow.engine.parser.FieldDeclaration;

public class ClassDelegateFactory {

    public ClassDelegate create(String className, List<FieldDeclaration> fieldDeclarations) {
        return new ClassDelegate(className, fieldDeclarations);
    }
}
