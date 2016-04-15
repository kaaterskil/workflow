package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.Expression;
import com.kaaterskil.workflow.bpm.common.event.ImplicitThrowEvent;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;

/**
 * This element controls when and which Events are thrown in case behavior of the Multi-Instance
 * Activity is set to complex.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ComplexBehaviorDefinition extends BaseElement {

    /**
     * This attribute defines a boolean Expression that when evaluated to true, cancels the
     * remaining Activity instances and produces a token.
     */
    @XmlElement(name = "condition", type = Expression.class, required = true)
    private Expression condition;

    /**
     * If the condition is true, this identifies the Event that will be thrown (to be caught by a
     * boundary Event on the Multi-Instance Activity).
     */
    @XmlElement(name = "event", type = ImplicitThrowEvent.class)
    private ImplicitThrowEvent event;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "ComplexBehaviorDefinition [condition=%s, event=%s, id=%s, documentation=%s, extensionElements=%s]",
                condition, event, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public ImplicitThrowEvent getEvent() {
        return event;
    }

    public void setEvent(ImplicitThrowEvent event) {
        this.event = event;
    }
}
