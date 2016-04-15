package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.Expression;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TimerEventDefinition extends EventDefinition {

    @XmlElement(name = "timeDate", type = Expression.class, required = false)
    private Expression timeDate;

    @XmlElement(name = "timeCycle", type = Expression.class, required = false)
    private Expression timeCycle;

    @XmlElement(name = "timeDuration", type = Expression.class, required = false)
    private Expression timeDuration;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "TimerEventDefinition [timeDate=%s, timeCycle=%s, timeDuration=%s, id=%s, documentation=%s, extensionElements=%s]",
                timeDate, timeCycle, timeDuration, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public Expression getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(Expression timeDate) {
        this.timeDate = timeDate;
    }

    public Expression getTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(Expression timeCycle) {
        this.timeCycle = timeCycle;
    }

    public Expression getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Expression timeDuration) {
        this.timeDuration = timeDuration;
    }

}
