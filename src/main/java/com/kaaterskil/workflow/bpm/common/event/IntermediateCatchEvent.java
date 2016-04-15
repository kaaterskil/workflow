package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IntermediateCatchEvent extends CatchEvent {

    @Override
    public String toString() {
        return String.format(
                "IntermediateCatchEvent [eventDefinitionRefs=%s, eventDefinitions=%s, dataOutputAssociations=%s, dataOutputs=%s, outputSet=%s, parallelMultiple=%s, properties=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                eventDefinitionRefs, eventDefinitions, dataOutputAssociations, dataOutputs,
                outputSet, parallelMultiple, properties, incoming, outgoing, name, categoryValueRef,
                id, documentation, extensionElements);
    }

}
