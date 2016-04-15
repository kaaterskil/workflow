package com.kaaterskil.workflow.bpm.common.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.Expression;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Assignment extends BaseElement {

    @XmlElement(name = "from", type = Expression.class, required = true)
    private Expression from;

    @XmlElement(name = "to", type = Expression.class, required = true)
    private Expression to;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format("Assignment [from=%s, to=%s, id=%s, documentation=%s, extensionElements=%s]",
                from, to, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public Expression getFrom() {
        return from;
    }

    public void setFrom(Expression from) {
        this.from = from;
    }

    public Expression getTo() {
        return to;
    }

    public void setTo(Expression to) {
        this.to = to;
    }
}
