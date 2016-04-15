package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.Expression;

/**
 * An Ad-Hoc Sub-Process is a specialized type of Sub-Process that is a group of Activities that
 * have no REQUIRED sequence relationships. A set of Activities can be defined for the Process, but
 * the sequence and number of performances for the Activities is determined by the performers of the
 * Activities.
 * <p>
 * Activities within the Process are generally disconnected from each other. During execution of the
 * Process, any one or more of the Activities MAY be active and they MAY be performed multiple
 * times. The performers determine when Activities will start, what the next Activity will be, and
 * so on.
 * <p>
 * Examples of the types of Processes that are Ad-Hoc include computer code development (at a low
 * level), sales support, and writing a book chapter. If we look at the details of writing a book
 * chapter, we could see that the Activities within this Process include: researching the topic,
 * writing text, editing text, generating graphics, including graphics in the text, organizing
 * references, etc. (see Figure 10.37). There MAY be some dependencies between Tasks in this
 * Process, such as writing text before editing text, but there is not necessarily any correlation
 * between an instance of writing text to an instance of editing text. Editing can occur
 * infrequently and based on the text of many instances of the writing text Task.
 * <p>
 * Although there is no explicit Process structure, some sequence and data dependencies can be added
 * to the details of the Process. For example, we can extend the book chapter Ad-Hoc Sub-Process
 * shown above and add Data Objects, Data Associations, and even Sequence Flows (Figure 10.38).
 * <p>
 * Ad-Hoc Sub-Processes restrict the use of BPMN elements that would normally be used in
 * Sub-Processes. 􏰀
 * <ul>
 * <li>The list of BPMN elements that MUST be used in an Ad-Hoc Sub-Process: Activity. 􏰀
 * <li>The list of BPMN elements that MAY be used in an Ad-Hoc Sub-Process: Data Object, Sequence
 * Flow, Association, Data Association, Group, Message Flow (as a source or target), Gateway, and
 * Intermediate Event. 􏰀
 * <li>The list of BPMN elements that MUST NOT be used in an Ad-Hoc Sub-Process: Start Event, End
 * Event, Conversations (graphically), Conversation Links (graphically), and Choreography
 * Activities.
 * </ul>
 * <p>
 * The Data Objects as inputs into the Tasks act as an additional constraint for the performance of
 * those Tasks. The performers still determine when the Tasks will be performed, but they are now
 * constrained in that they cannot start the Task without the appropriate input. The addition of
 * Sequence Flows between the Tasks (e.g., between “Generate Graphics” and “Include Graphics in
 * Text”) creates a dependency where the performance of the first Task MUST be followed by a
 * performance of the second Task. This does not mean that the second Task is to be performed
 * immediately, but there MUST be a performance of the second Task after the performance of the
 * first Task.
 * <p>
 * It is a challenge for a BPM engine to monitor the status of Ad-Hoc Sub-Processes, usually these
 * kind of Processes are handled through groupware applications (such as e-mail), but BPMN allows
 * modeling of Processes that are not necessarily executable, although there are some process
 * engines that can follow an Ad-Hoc Sub-Process. Given this, at some point the Ad-Hoc Sub-Process
 * will have complete and this can be determined by evaluating a completionCondition that evaluates
 * Process attributes that will have been updated by an Activity in the Process.
 * </p>
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class AdHocSubProcess extends SubProcess {

    /**
     * This Expression defines the conditions when the Process will end. When the Expression is
     * evaluated to true, the Process will be terminated.
     */
    @XmlElement(name = "completionCondition", type = Expression.class, required = false)
    private Expression completionCondition;

    /**
     * This attribute defines if the Activities within the Process can be performed in parallel or
     * MUST be performed sequentially. The default setting is parallel and the setting of sequential
     * is a restriction on the performance that can be needed due to shared resources. When the
     * setting is sequential, then only one Activity can be performed at a time. When the setting is
     * parallel, then zero (0) to all the Activities of the Sub-Process can be performed in
     * parallel.
     */
    @XmlAttribute
    private AdHocOrdering ordering = AdHocOrdering.PARALLEL;

    /**
     * This attribute is used only if ordering is parallel. It determines whether running instances
     * are cancelled when the completionCondition becomes true.
     */
    @XmlAttribute
    private boolean cancelRemainingInstances = true;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "AdHocSubProcess [completionCondition=%s, ordering=%s, cancelRemainingInstances=%s, flowElements=%s, artifacts=%s, isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                completionCondition, ordering, cancelRemainingInstances, flowElements, artifacts,
                isForCompensation, loopCharacteristics, defaultSequenceFlow, ioSpecification,
                properties, dataInputAssociations, dataOutputAssociations, startQuantity,
                completionQuantity, state, incoming, outgoing, name, categoryValueRef, id,
                documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public Expression getCompletionCondition() {
        return completionCondition;
    }

    public void setCompletionCondition(Expression completionCondition) {
        this.completionCondition = completionCondition;
    }

    public AdHocOrdering getOrdering() {
        return ordering;
    }

    public void setOrdering(AdHocOrdering ordering) {
        this.ordering = ordering;
    }

    public boolean isCancelRemainingInstances() {
        return cancelRemainingInstances;
    }

    public void setCancelRemainingInstances(boolean cancelRemainingInstances) {
        this.cancelRemainingInstances = cancelRemainingInstances;
    }
}
