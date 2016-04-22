package com.kaaterskil.workflow.engine.parser.factory;

import java.util.ArrayList;
import java.util.List;

import com.kaaterskil.workflow.bpm.FieldExtension;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.parser.FieldDeclaration;
import com.kaaterskil.workflow.util.CollectionUtil;

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

        if (CollectionUtil.isNotEmpty(element.getExtensionElements())) {
            for (final Object each : element.getExtensionElements()) {
                if (each instanceof FieldExtension) {
                    fields.add((FieldExtension) each);
                }
            }
        }
        return fields;
    }
}
