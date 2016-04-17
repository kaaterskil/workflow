package com.kaaterskil.workflow.engine.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

public abstract class AbstractBehaviorFactory {

    public List<FieldDeclaration> createFieldDeclarations(Map<QName, Object> elementAttributes) {
        final List<FieldDeclaration> fieldDeclarations = new ArrayList<>();
        if(elementAttributes != null && !elementAttributes.isEmpty()) {
            for(final Entry<QName, Object> entry : elementAttributes.entrySet()) {
                if(entry != null) {
                    final String name = entry.getKey().getLocalPart();
                    final Object value = entry.getValue();
                    final String type = value.getClass().getCanonicalName();

                    final FieldDeclaration declaration = new FieldDeclaration(name, type, value);
                    fieldDeclarations.add(declaration);
                }
            }
        }
        return fieldDeclarations;
    }
}
