package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Task is an atomic Activity within a Process flow. A Task is used when the work in the Process
 * cannot be broken down to a finer level of detail. Generally, an end-user and/or applications are
 * used to perform the Task when it is executed.
 * <p>
 * There are different types of Tasks identified within BPMN to separate the types of inherent
 * behavior that Tasks might represent. The list of Task types MAY be extended along with any
 * corresponding indicators. A Task which is not further specified is called Abstract Task (this was
 * referred to as the None Task in BPMN 1.2).
 *
 * @author bcaple
 */
@XmlRootElement
public class Task extends Activity {

    @Override
    public String toString() {
        return String.format(
                "Task [isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                isForCompensation, loopCharacteristics, defaultSequenceFlow, ioSpecification,
                properties, dataInputAssociations, dataOutputAssociations, startQuantity,
                completionQuantity, state, incoming, outgoing, name, categoryValueRef, id,
                documentation, extensionElements);
    }

}
