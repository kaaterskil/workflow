package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * As the name implies, the Start Event indicates where a particular Process will start. In terms of
 * Sequence Flows, the Start Event starts the flow of the Process, and thus, will not have any
 * incoming Sequence Flows—no Sequence Flow can connect to a Start Event.
 * <p>
 * Throughout this document, we discuss how Sequence Flows are used within a Process. To facilitate
 * this discussion, we employ the concept of a token that will traverse the Sequence Flows and pass
 * through the elements in the Process. A token is a theoretical concept that is used as an aid to
 * define the behavior of a Process that is being performed. The behavior of Process elements can be
 * defined by describing how they interact with a token as it “traverses” the structure of the
 * Process.
 * <p>
 * Note – A token does not traverse a Message Flow since it is a Message that is passed down a
 * Message Flow (as the name implies).
 * <p>
 * Semantics of the Start Event include: 􏰀
 * <ul>
 * <li>A Start Event is OPTIONAL: a Process level—a top-level Process, a Sub-Process (embedded), or
 * a Global Process (called Process)—MAY (is NOT REQUIRED to) have a Start Event.
 * </ul>
 * <p>
 * Note – A Process MAY have more than one Process level (i.e., it can include Expanded
 * Sub-Processes or Call Activities that call other Processes). The use of Start and End Events is
 * independent for each level of the Diagram.
 * <ul>
 * 􏰀
 * <li>If a Process is complex and/or the starting conditions are not obvious, then it is
 * RECOMMENDED that a Start Event be used. 􏰀
 * <li>If a Start Event is not used, then the implicit Start Event for the Process SHALL NOT have a
 * trigger. 􏰀
 * <li>If there is an End Event, then there MUST be at least one Start Event. 􏰀
 * <li>All Flow Objects that do not have an incoming Sequence Flow (i.e., are not a target of a
 * Sequence Flow) SHALL be instantiated when the Process is instantiated. 􏰀
 * <ul>
 * <li>Exceptions to this are Activities that are defined as being Compensation Activities (it has
 * the Compensation marker). Compensation Activities are not considered a part of the normal flow
 * and MUST NOT be instantiated when the Process is instantiated. See page 302 for more information
 * on Compensation Activities. 􏰀
 * <li>An exception to this is a catching Link Intermediate Event, which is not allowed to have
 * incoming Sequence Flows. See page 267 for more information on Link Intermediate Events. 􏰀
 * <li>An exception to this is an Event Sub-Process, which is not allowed to have incoming Sequence
 * Flows and will only be instantiated when its Start Event is triggered. See page 176 for more
 * information on Event Sub-Processes. 􏰀
 * </ul>
 * <li>There MAY be multiple Start Events for a given Process level. 􏰀
 * <ul>
 * <li>Each Start Event is an independent Event. That is, a Process instance SHALL be generated when
 * the Start Event is triggered.
 * </ul>
 * </ul>
 * <p>
 * If the Process is used as a global Process (a callable Process that can be invoked from Call
 * Activities of other Processes) and there are multiple None Start Events, then when flow is
 * transferred from the parent Process to the global Process, only one of the global Process’s Start
 * Events will be triggered. The targetRef attribute of a Sequence Flow incoming to the Call
 * Activity object can be extended to identify the appropriate Start Event.
 * <p>
 * Note – The behavior of Process can be harder to understand if there are multiple Start Events. It
 * is RECOMMENDED that this feature be used sparingly and that the modeler be aware that other
 * readers of the Diagram could have difficulty understanding the intent of the Diagram.
 * <p>
 * When the trigger for a Start Event occurs, a new Process will be instantiated and a token will be
 * generated for each outgoing Sequence Flow from that Event.
 * <p>
 * <b>Start Event Triggers</b>
 * <p>
 * Start Events can be used for these types of Processes:
 * <ul>
 * <li>Top-level Processes
 * <li>Sub-Processes (embedded)
 * <li>Global Process (called)
 * <li>Event Sub-Processes
 * </ul>
 * <p>
 * There are many ways that top-level Processes can be started (instantiated). The trigger for a
 * Start Event is designed to show the general mechanisms that will instantiate that particular
 * Process. There are seven (7) types of Start Events for top-level Processes in BPMN (see Table
 * 10.84): None, Message, Timer, Conditional, Signal, Multiple, and Parallel.
 * <p>
 * A top-level Process that has at least one None Start Event MAY be called by a Call Activity in
 * another Process. The None Start Event is used for invoking the Process from the Call Activity.
 * All other types of Start Events are only applicable when the Process is used as a top-level
 * Process.
 * <p>
 * There is only one type of Start Event for Sub-Processes in BPMN (see Figure 10.82): None.
 * <p>
 * A Start Event can also initiate an inline Event Sub-Process (see page 176). In that case, the
 * same Event types as for boundary Events are allowed (see Table 10.86), namely: Message, Timer,
 * Escalation, Error, Compensation, Conditional, Signal, Multiple, and Parallel. 􏰀
 * <ul>
 * <li>An Event Sub-Process MUST have a single Start Event.
 * </ul>
 * <p>
 * <b>SequenceFlow Conditions</b>
 * <ul>
 * <li>A Start Event MUST NOT be a target for Sequence Flows; it MUST NOT have incoming Sequence
 * Flows. 􏰀
 * <ul>
 * <li>An exception to this is when a Start Event is used in an Expanded Sub-Process and is attached
 * to the boundary of that Sub-Process. In this case, a Sequence Flow from the higher-level Process
 * MAY connect to that Start Event in lieu of connecting to the actual boundary of the Sub-Process.
 * 􏰀
 * </ul>
 * <li>A Start Event MUST be a source for a Sequence Flow. 􏰀
 * <li>Multiple Sequence Flows MAY originate from a Start Event. For each Sequence Flow that has the
 * Start Event as a source, a new parallel path SHALL be generated. 􏰀
 * <ul>
 * <li>The conditionExpression attribute for all outgoing Sequence Flows MUST be set to None. 􏰀
 * <li>When a Start Event is not used, then all Flow Objects that do not have an incoming Sequence
 * Flow SHALL be the start of a separate parallel path. 􏰀
 * <li>Each path will have a separate unique token that will traverse the Sequence Flow.
 * </ul>
 * </ul>
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class StartEvent extends CatchEvent {

    /**
     * This attribute only applies to Start Events of Event Sub-Processes; it is ignored for other
     * Start Events. This attribute denotes whether the Sub-Process encompassing the Event
     * Sub-Process should be cancelled or not, If the encompassing Sub-Process is not cancelled,
     * multiple instances of the Event Sub-Process can run concurrently. This attribute cannot be
     * applied to Error Events (where it’s always true), or Compensation Events (where it doesn’t
     * apply).
     */
    @XmlAttribute
    private boolean isInterrupting = true;

    @Override
    public String toString() {
        return String.format(
                "StartEvent [isInterrupting=%s, eventDefinitionRefs=%s, eventDefinitions=%s, dataOutputAssociations=%s, dataOutputs=%s, outputSet=%s, parallelMultiple=%s, properties=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                isInterrupting, eventDefinitionRefs, eventDefinitions, dataOutputAssociations,
                dataOutputs, outputSet, parallelMultiple, properties, incoming, outgoing, name,
                categoryValueRef, id, documentation, extensionElements);
    }

    public boolean isInterrupting() {
        return isInterrupting;
    }

    public void setInterrupting(boolean isInterrupting) {
        this.isInterrupting = isInterrupting;
    }
}
