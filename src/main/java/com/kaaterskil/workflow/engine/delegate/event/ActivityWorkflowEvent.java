package com.kaaterskil.workflow.engine.delegate.event;

import java.io.Serializable;

public class ActivityWorkflowEvent extends BaseWorkflowEvent {

    protected Serializable activityId;
    protected String activityName;
    protected String activityType;
    protected String behaviorClass;

    public ActivityWorkflowEvent(WorkflowEventType type) {
        super(type);
    }

    public Serializable getActivityId() {
        return activityId;
    }

    public void setActivityId(Serializable activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getBehaviorClass() {
        return behaviorClass;
    }

    public void setBehaviorClass(String behaviorClass) {
        this.behaviorClass = behaviorClass;
    }
}
