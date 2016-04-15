package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.Expression;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ConditionalEventDefinition extends EventDefinition {

    @XmlElement(name = "condition", type = Expression.class, required = true)
    private Expression condition;

    @Override
    public String toString() {
        return String.format(
                "ConditionalEventDefinition [condition=%s, id=%s, documentation=%s, extensionElements=%s]",
                condition, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }
}
