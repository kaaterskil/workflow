package com.kaaterskil.workflow.engine.parser.factory;

import java.util.ArrayList;
import java.util.List;

import com.kaaterskil.workflow.bpm.FieldExtension;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.parser.FieldDeclaration;

public abstract class AbstractBehaviorFactory {

    public List<FieldDeclaration> createFieldDeclarations(List<FieldExtension> fields) {
        final List<FieldDeclaration> fieldDeclarations = new ArrayList<>();

        for (final FieldExtension field : fields) {
            final FieldDeclaration declaration = new FieldDeclaration(field.getFieldName(), null,
                    field.getValue());
            fieldDeclarations.add(declaration);
        }
        return fieldDeclarations;
    }

    protected List<FieldExtension> extractFieldExtensions(BaseElement element) {
        final List<FieldExtension> fields = new ArrayList<>();

        if (element.getExtensionElements() != null && !element.getExtensionElements().isEmpty()) {
            for (final Object each : element.getExtensionElements()) {
                if (each instanceof FieldExtension) {
                    fields.add((FieldExtension) each);
                }
            }
        }
        return fields;
    }
}
