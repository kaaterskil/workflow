package com.kaaterskil.workflow.engine.variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableTypeHelper {

    private final List<VariableType> store = new ArrayList<>();
    private final Map<String, VariableType> typedStore = new HashMap<>();

    public void addType(VariableType type) {
        store.add(type);
        typedStore.put(type.getType(), type);
    }

    public VariableType getType(String typeName) {
        return typedStore.get(typeName);
    }

    public VariableType findTypeForValue(Object value) {
        for(final VariableType type : store) {
            if(type.supports(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Cannot serialize the given value " + value.toString());
    }
}
