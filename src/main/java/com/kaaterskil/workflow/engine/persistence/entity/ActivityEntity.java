package com.kaaterskil.workflow.engine.persistence.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "wf_hist_activities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 30)
public class ActivityEntity {

    @Id
    @SequenceGenerator(name = "wf_hist_activities_id_seq_gen",
                       sequenceName = "wf_hist_activities_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wf_hist_activities_id_seq_gen")
    @Column(name = "activity_id")
    private Long id;

    @Column(name = "process_definition_id", nullable = false)
    private Long processDefinitionId;

    @Column(name = "process_instance_id", nullable = false)
    private Long processInstanceId;

    @Column(name = "token_id", nullable = false)
    private Long tokenId;

    @Column(name = "key", length = 255)
    private String key;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started_at", nullable = false)
    private Date startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ended_at")
    private Date endedAt;

    @Column(name = "duration")
    private long duration;

    /*---------- Methods ----------*/

    public void markEnded() {
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        this.endedAt = c.getTime();
        this.duration = this.endedAt.getTime() - this.startedAt.getTime();
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

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
