package com.kaaterskil.workflow.bpm.common.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Event-Based Gateway represents a branching point in the Process where the alternative paths
 * that follow the Gateway are based on Events that occur, rather than the evaluation of Expressions
 * using Process data (as with an Exclusive or Inclusive Gateway). A specific Event, usually the
 * receipt of a Message, determines the path that will be taken. Basically, the decision is made by
 * another Participant, based on data that is not visible to Process, thus, requiring the use of the
 * Event-Based Gateway.
 * <p>
 * For example, if a company is waiting for a response from a customer they will perform one set of
 * Activities if the customer responds “Yes” and another set of Activities if the customer responds
 * “No.” The customer’s response determines which path is taken. The identity of the Message
 * determines which path is taken. That is, the “Yes” Message and the “No” Message are different
 * Messages—i.e., they are not the same Message with different values within a property of the
 * Message. The receipt of the Message can be modeled with an Intermediate Event with a Message
 * trigger or a Receive Task. In addition to Messages, other triggers for Intermediate Events can be
 * used, such as Timers.
 * <p>
 * Unlike other Gateways, the behavior of the Event Gateway is determined by a configuration of
 * elements, rather than the single Gateway. 􏰀
 * <ul>
 * <li>An Event Gateway MUST have two or more outgoing Sequence Flows. 􏰀
 * <li>The outgoing Sequence Flows of the Event Gateway MUST NOT have a conditionExpression. The
 * objects that are on the target end of the Gateway’s outgoing Sequence Flows are part of the
 * configuration of the Gateway.
 * <li>􏰀Event-Based Gateways are configured by having outgoing Sequence Flows target an
 * Intermediate Event or a Receive Task in any combination (see Figure 10.116 and Figure 10.117)
 * except that: 􏰀
 * <ul>
 * <li>If Message Intermediate Events are used in the configuration, then Receive Tasks MUST NOT be
 * used in that configuration and vice versa. 􏰀
 * <li>Receive Tasks used in an Event Gateway configuration MUST NOT have any attached Intermediate
 * Events. 􏰀
 * <li>Only the following Intermediate Event triggers are valid: Message, Signal, Timer,
 * Conditional, and Multiple (which can only include the previous triggers). Thus, the following
 * Intermediate Event triggers are not valid: Error, Cancel, Compensation, and Link. 􏰀
 * </ul>
 * <li>Target elements in an Event Gateway configuration MUST NOT have any additional incoming
 * Sequence Flows (other than that from the Event Gateway).
 * </ul>
 * <p>
 * When the first Event in the Event Gateway configuration is triggered, then the path that follows
 * that Event will used (a token will be sent down the Event’s outgoing Sequence Flows). All the
 * remaining paths of the Event Gateway configuration will no longer be valid. Basically, the Event
 * Gateway configuration is a race condition where the first Event that is triggered wins.
 * <p>
 * There are variations of the Event Gateway that can be used at the start of the Process. The
 * behavior and marker of the Gateway will change.
 * <p>
 * Event Gateways can be used to instantiate a Process. By default the Gateway’s instantiate
 * attribute is false, but if set to true, then the Process is instantiated when the first Event of
 * the Gateway’s configuration is triggered. 􏰀
 * <ul>
 * <li>If the Event Gateway’s instantiate attribute is set to true, then the marker for the Event
 * Gateway looks like a Multiple Start Event (see Figure 10.118).
 * </ul>
 * <p>
 * In order for an Event Gateway to instantiate a Process, it MUST not have any incoming Sequence
 * Flows.
 * <p>
 * In some situations a modeler might want the Process to be instantiated by one of a set of
 * Messages while still requiring all of the Messages for the working of the same Process instance.
 * To handle this, there is another variation of the Event Gateway. 􏰀
 * <ul>
 * <li>If the Event Gateway’s instantiate attribute is set to true and the eventGatewayType
 * attribute is set to Parallel, then the marker for the Event Gateway looks like a Parallel
 * Multiple Start Event (see Figure 10.119). 􏰀
 * <li>The Event Gateway’s instantiate attribute MUST be set to true in order for the
 * eventGatewayType attribute to be set to Parallel (i.e., for Event Gateway’s that do not
 * instantiate the Process MUST be Exclusive—a standard Parallel Gateway can be used to include
 * parallel Events in the middle of a Process).
 * </ul>
 * <p>
 * The Parallel Event Gateway is also a type of race condition. In this case, however, when the
 * first Event is triggered and the Process is instantiated, the other Events of the Gateway
 * configuration are not disabled. The other Events are still waiting and are expected to be
 * triggered before the Process can (normally) complete. In this case, the Messages that trigger the
 * Events of the Gateway configuration MUST share the same correlation information.
 * <p>
 * Event-Based Gateways can be used at the start of a Process, without having to be a target of
 * Sequence Flows. There can be multiple such Event-Based Gateways at the start of a Process.
 * Ordinary Start Events and Event- Based Gateways can be used together.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EventBasedGateway extends Gateway {

    @XmlAttribute
    private boolean instantiate = false;

    @XmlAttribute
    private EventBasedGatewayType eventGatewayType = EventBasedGatewayType.EXCLUSIVE;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "EventBasedGateway [instantiate=%s, eventGatewayType=%s, gatewayDirection=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                instantiate, eventGatewayType, gatewayDirection, incoming, outgoing, name,
                categoryValueRef, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public boolean isInstantiate() {
        return instantiate;
    }

    public void setInstantiate(boolean instantiate) {
        this.instantiate = instantiate;
    }

    public EventBasedGatewayType getEventGatewayType() {
        return eventGatewayType;
    }

    public void setEventGatewayType(EventBasedGatewayType eventGatewayType) {
        this.eventGatewayType = eventGatewayType;
    }
}
