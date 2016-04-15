package com.kaaterskil.workflow.engine.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kaaterskil.workflow.bpm.foundation.ExtensionAttribute;

public abstract class AbstractBehaviorFactory {

    public List<FieldDeclaration> createFieldDeclarations(
            Collection<ExtensionAttribute> collection) {
        final List<FieldDeclaration> fieldDeclarations = new ArrayList<>();
        if (collection != null && !collection.isEmpty()) {
            for (final ExtensionAttribute attribute : collection) {
                if (attribute != null) {
                    final FieldDeclaration declaration = new FieldDeclaration(attribute.getName(),
                            attribute.getType(), attribute.getValue());
                    fieldDeclarations.add(declaration);
                }
            }
        }

        return fieldDeclarations;
    }
}
