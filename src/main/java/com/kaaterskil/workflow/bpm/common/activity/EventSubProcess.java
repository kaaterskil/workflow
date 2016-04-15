package com.kaaterskil.workflow.bpm.common.activity;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.FlowElement;

/**
 * An Event Sub-Process is a specialized Sub-Process that is used within a Process (or Sub-Process).
 * A Sub- Process is defined as an Event Sub-Process when its triggeredByEvent attribute is set to
 * true.
 * <p>
 * An Event Sub-Process is not part of the normal flow of its parent Process—there are no incoming
 * or outgoing Sequence Flows. 􏰀 An Event Sub-Process MUST NOT have any incoming or outgoing
 * Sequence Flows.
 * <p>
 * An Event Sub-Process MAY or MAY NOT occur while the parent Process is active, but it is possible
 * that it will occur many times. Unlike a standard Sub-Process, which uses the flow of the parent
 * Process as a trigger, an Event Sub-Process has a Start Event with a trigger. Each time the Start
 * Event is triggered while the parent Process is active, then the Event Sub-Process will start. 􏰀
 * <ul>
 * <li>The Start Event of an Event Sub-Process MUST have a defined trigger. 􏰀
 * <li>The Start Event trigger (EventDefinition) MUST be from the following types: Message, Error,
 * Escalation, Compensation, Conditional, Signal, and Multiple (see page 260 for more details). 􏰀
 * <li>An Event Sub-Process MUST have one and only one Start Event.
 * </ul>
 * <p>
 * There are two possible consequences to the parent Process when an Event Sub-Process is triggered:
 * 1) the parent Process can be interrupted, and 2) the parent Process can continue its work (not
 * interrupted). This is determined by the type of Start Event that is used. See page 242 for the
 * list of interrupting and non-interrupting Event Sub-Process Start Events.
 * </p>
 *
 * @author bcaple
 */
@XmlRootElement
public class EventSubProcess extends SubProcess {

    @Override
    public FlowElement getFlowElement(String flowElementId, boolean searchRecursively) {
        return null;
    }

    @Override
    public void addFlowElement(FlowElement flowElement) {
        // Noop
    }

    /*---------- Getter/Setters ----------*/

    @Override
    public boolean isTriggeredByEvent() {
        return true;
    }

    @Override
    public void setFlowElements(List<FlowElement> flowElements) {
        // Noop
    }

}
