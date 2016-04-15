package com.kaaterskil.workflow.engine.persistence.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "wf_hist_process_instances")
public class ProcessInstanceEntity {

    @Id
    @Column(name = "process_instance_id", nullable = false)
    private Long id;

    @Column(name = "process_definition_id", nullable = false)
    private Long processDefinitionId;

    @Column(name = "runtime_process_instance_id", nullable = false)
    private Long processInstanceId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "start_activity_id", length = 255)
    private String startActivityId;

    @Column(name = "end_activity_id", length = 255)
    private String endActivityId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started_at", nullable = false)
    private Date startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ended_at")
    private Date endedAt;

    @Column(name = "duration")
    private long duration;

    /*---------- Constructors ----------*/

    public ProcessInstanceEntity(Token token) {
        id = token.getId();
        processInstanceId = token.getId();
        processDefinitionId = token.getProcessDefinitionId();
        startActivityId = token.getCurrentActivity();

        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        startedAt = c.getTime();
    }

    public ProcessInstanceEntity() {
    }

    /*---------- Getter/Setters ----------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(Long processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartActivityId() {
        return startActivityId;
    }

    public void setStartActivityId(String startActivityId) {
        this.startActivityId = startActivityId;
    }

    public String getEndActivityId() {
        return endActivityId;
    }

    public void setEndActivityId(String endActivityId) {
        this.endActivityId = endActivityId;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
