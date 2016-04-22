package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EndEvent extends ThrowEvent {

    @Override
    public String toString() {
        return String.format(
                "EndEvent [eventDefinitionRefs=%s, eventDefinitions=%s, dataInputAssociations=%s, dataInputs=%s, inputSet=%s, properties=%s, incoming=%s, outgoing=%s, isAsynchronous=%s, behavior=%s, name=%s, categoryValueRef=%s, tokenListeners=%s, subProcess=%s, id=%s, documentation=%s, extensionElements=%s, extensionAttributes=%s]",
                eventDefinitionRefs, eventDefinitions, dataInputAssociations, dataInputs, inputSet,
                properties, incoming, outgoing, isAsynchronous, behavior, name, categoryValueRef,
                tokenListeners, subProcess, id, documentation, extensionElements,
                extensionAttributes);
    }

}
