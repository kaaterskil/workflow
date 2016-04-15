package com.kaaterskil.workflow.engine.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "wf_run_event_subscriptions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type", length = 30, discriminatorType = DiscriminatorType.STRING)
public abstract class EventSubscription {

    @Id
    @SequenceGenerator(name = "wf_run_event_subs_id_seq_gen",
                       sequenceName = "wf_run_event_subs_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wf_run_event_subs_id_seq_gen")
    @Column(name = "event_subscription_id")
    protected Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", insertable = false, updatable = false)
    protected EventSubscriptionType eventType;

    @Column(name = "event_name", length = 255)
    protected String eventName;

    @ManyToOne
    @JoinColumn(name = "token_id")
    protected Token token;

    @Column(name = "process_instance_id")
    protected Long processInstanceId;

    @Column(name = "process_definition_id")
    protected Long processDefinitionId;

    @Column(name = "activity_id", length = 255)
    protected String activityId;

    @Column(name = "configuration", length = 255)
    protected String configuration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    protected Date createdAt;

    @Version
    @Column(name = "version")
    protected int version = 1;

    /*---------- Getter/Setters ----------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventSubscriptionType getEventType() {
        return eventType;
    }

    public void setEventType(EventSubscriptionType eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(Long processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
