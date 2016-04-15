package com.kaaterskil.workflow.engine.persistence.entity;

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
import javax.persistence.Transient;

@Entity
@Table(name = "wf_run_jobs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 30)
public class JobEntity {

    @Id
    @SequenceGenerator(name = "wf_run_jobs_id_seq_gen",
                       sequenceName = "wf_run_jobs_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wf_run_jobs_id_seq_gen")
    @Column(name = "job_id")
    private Long id;

    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @Column(name = "process_definition_id")
    private Long processDefinitionId;

    @Column(name = "process_instance_id")
    private Long processInstanceId;

    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "retries")
    private int retries;

    @Column(name = "due_by")
    private Date dueBy;

    @Column(name = "lock_expires_at")
    private Date lockExpiresAt;

    @Column(name = "lock_owner", length = 255)
    private String lockOwner;

    @Column(name = "is_exclusive")
    private boolean isExclusive;

    @Column(name = "repeat", length = 255)
    private String repeat;

    @Column(name = "exception_message", length = 4000)
    private String exceptionMessage;

    @Column(name = "exception_stack_trace")
    private String exceptionStackTrace;

    /*---------- Instance properties ----------*/

    @Transient
    private String jobHandlerType;

    @Transient
    private Long jobHandlerConfiguration;

    public String getJobHandlerType() {
        return jobHandlerType;
    }

    public void setJobHandlerType(String jobHandlerType) {
        this.jobHandlerType = jobHandlerType;
    }

    public Long getJobHandlerConfiguration() {
        return jobHandlerConfiguration;
    }

    public void setJobHandlerConfiguration(Long jobHandlerConfiguration) {
        this.jobHandlerConfiguration = jobHandlerConfiguration;
    }

    /*---------- Getter/Setters ----------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public Date getDueBy() {
        return dueBy;
    }

    public void setDueBy(Date dueBy) {
        this.dueBy = dueBy;
    }

    public Date getLockExpiresAt() {
        return lockExpiresAt;
    }

    public void setLockExpiresAt(Date lockExpiresAt) {
        this.lockExpiresAt = lockExpiresAt;
    }

    public String getLockOwner() {
        return lockOwner;
    }

    public void setLockOwner(String lockOwner) {
        this.lockOwner = lockOwner;
    }

    public boolean isExclusive() {
        return isExclusive;
    }

    public void setExclusive(boolean isExclusive) {
        this.isExclusive = isExclusive;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }
}
