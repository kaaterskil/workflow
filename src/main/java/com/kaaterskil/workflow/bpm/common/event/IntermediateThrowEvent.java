package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IntermediateThrowEvent extends ThrowEvent {

    @Override
    public String toString() {
        return String.format(
                "IntermediateThrowEvent [eventDefinitionRefs=%s, eventDefinitions=%s, dataInputAssociations=%s, dataInputs=%s, inputSet=%s, properties=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                eventDefinitionRefs, eventDefinitions, dataInputAssociations, dataInputs, inputSet,
                properties, incoming, outgoing, name, categoryValueRef, id, documentation,
                extensionElements);
    }

}
