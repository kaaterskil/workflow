package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CompensateEventDefinition extends EventDefinition {

    @XmlAttribute
    private String activityRef;

    @XmlAttribute
    private boolean waitForCompletion = true;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "CompensateEventDefinition [activityRef=%s, waitForCompensation=%s, id=%s, documentation=%s, extensionElements=%s]",
                activityRef, waitForCompletion, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getActivityRef() {
        return activityRef;
    }

    public void setActivityRef(String activityRef) {
        this.activityRef = activityRef;
    }

    public boolean isWaitForCompletion() {
        return waitForCompletion;
    }

    public void setWaitForCompletion(boolean waitForCompletion) {
        this.waitForCompletion = waitForCompletion;
    }
}
