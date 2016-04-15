package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NoneTask extends Task {

    @Override
    public String toString() {
        return String.format(
                "NoneTask [isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                isForCompensation, loopCharacteristics, defaultSequenceFlow, ioSpecification,
                properties, dataInputAssociations, dataOutputAssociations, startQuantity,
                completionQuantity, state, incoming, outgoing, name, categoryValueRef, id, documentation,
                extensionElements);
    }

}
