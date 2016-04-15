package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TerminateEventDefinition extends EventDefinition {

    @Override
    public String toString() {
        return String.format(
                "TerminateEventDefinition [id=%s, documentation=%s, extensionElements=%s]", id,
                documentation, extensionElements);
    }

}
