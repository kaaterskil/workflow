package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Call Activity identifies a point in the Process where a global Process or a Global Task is
 * used. The Call Activity acts as a ‘wrapper’ for the invocation of a global Process or Global Task
 * within the execution. The activation of a call Activity results in the transfer of control to the
 * called global Process or Global Task. The BPMN 2.0 Call Activity corresponds to the Reusable
 * Sub-Process of BPMN 1.2. A BPMN 2.0 Sub-Process corresponds to the Embedded Sub-Process of BPMN
 * 1.2 (see the previous section).
 * <p>
 * A Call Activity MUST fulfill the data requirements, as well as return the data produced by the
 * CallableElement being invoked (see Figure 10.41). This means that the elements contained in the
 * Call Activity’s InputOutputSpecification MUST exactly match the elements contained in the
 * referenced CallableElement. This includes DataInputs, DataOutputs, InputSets, and OutputSets.
 * <p>
 * A Call Activity can override properties and attributes of the element being called, potentially
 * changing the behavior of the called element based on the calling context. For example, when the
 * Call Activity defines one or more ResourceRole elements, the elements defined by the
 * CallableElement are ignored and the elements defined in the Call Activity are used instead. Also,
 * Events that are propagated along the hierarchy (errors and escalations) are propagated from the
 * called element to the Call Activity (and can be handled on its boundary).
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CallActivity extends Activity {

    /**
     * The element to be called, which will be either a Process or a GlobalTask. Other
     * CallableElements, such as Choreography, GlobalChoreographyTask, Conversation, and
     * GlobalCommunication MUST NOT be called by the Call Conversation element.
     */
    @XmlAttribute
    private String calledElement;

    @Override
    public String toString() {
        return String.format(
                "CallActivity [calledElement=%s, isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                calledElement, isForCompensation, loopCharacteristics, defaultSequenceFlow,
                ioSpecification, properties, dataInputAssociations, dataOutputAssociations,
                startQuantity, completionQuantity, state, incoming, outgoing, name,
                categoryValueRef, id, documentation, extensionElements);
    }

    public String getCalledElement() {
        return calledElement;
    }

    public void setCalledElement(String calledElement) {
        this.calledElement = calledElement;
    }
}
