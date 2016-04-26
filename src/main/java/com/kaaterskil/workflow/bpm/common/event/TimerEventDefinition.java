package com.kaaterskil.workflow.bpm.common.event;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TimerEventDefinition extends EventDefinition {

    @XmlElement(name = "timeDate", type = Date.class, required = false)
    private Date timeDate;

    @XmlElement(name = "timeCycle", type = Long.class, required = false)
    private Long timeCycle;

    @XmlElement(name = "timeDuration", type = Long.class, required = false)
    private Long timeDuration;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "TimerEventDefinition [timeDate=%s, timeCycle=%s, timeDuration=%s, id=%s, documentation=%s, extensionElements=%s]",
                timeDate, timeCycle, timeDuration, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public Date getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(Date timeDate) {
        this.timeDate = timeDate;
    }

    public Long getTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(Long timeCycle) {
        this.timeCycle = timeCycle;
    }

    public Long getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Long timeDuration) {
        this.timeDuration = timeDuration;
    }

}
