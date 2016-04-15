package com.kaaterskil.workflow.engine.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.persistence.mapper.VariableMapper;
import com.kaaterskil.workflow.engine.runtime.ProcessInstance;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;

@Entity
@Table(name = "wf_run_tokens")
public class Token implements ProcessInstance, DelegateToken {

    @Id
    @SequenceGenerator(name = "wf_run_tokens_id_seq_gen",
                       sequenceName = "wf_run_tokens_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wf_run_tokens_id_seq_gen")
    @Column(name = "token_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Token parent;

    @ManyToOne
    @JoinColumn(name = "process_definition_id")
    private ProcessDefinitionEntity processDefinition;

    @Column(name = "root_process_instance_id")
    private Long rootProcessInstanceId;

    @Column(name = "process_instance_id")
    private Long processInstanceId;

    @Column(name = "activity_id", length = 255)
    private String currentActivity;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_scope")
    private boolean isScope;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lock_time")
    private Date lockTime;

    @OneToMany(mappedBy = "tokenId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKey(name = "tokenId")
    private final Map<String, VariableEntity> variables = new HashMap<>();

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Token> childTokens = new ArrayList<>();

    @OneToMany(mappedBy = "token", fetch = FetchType.EAGER)
    private List<EventSubscription> eventSubscriptions = new ArrayList<>();

    /*---------- Instance properties ----------*/

    @Transient
    private FlowElement currentFlowElement;

    @Transient
    private boolean isEnded;

    @Transient
    private boolean isDeleted;

    @Transient
    private boolean isEventScope;

    @Transient
    private String eventName;

    @Transient
    private List<JobEntity> jobs;

    @Transient
    private Token subProcessInstance;

    /*---------- Methods ----------*/

    @Override
    public Long getParentId() {
        if(parent != null) {
            return parent.getId();
        }
        return null;
    }

    @Override
    public String getActivityId() {
        if(currentActivity != null) {
            return currentActivity;
        }
        return null;
    }

    @Override
    public Long getProcessDefinitionId() {
        if (processDefinition != null) {
            return processDefinition.getId();
        }
        return null;
    }

    @Override
    public String getProcessDefinitionKey() {
        if (processDefinition != null) {
            return processDefinition.getKey();
        }
        return null;
    }

    @Override
    public String getProcessDefinitionName() {
        if (processDefinition != null) {
            return processDefinition.getName();
        }
        return null;
    }

    @Override
    public int getProcessDefinitionVersion() {
        if (processDefinition != null) {
            return processDefinition.getVersion();
        }
        return 0;
    }

    @Override
    public Object getVariable(String variableName) {
        return variables.get(variableName);
    }

    @Override
    public void setVariable(String variableName, Object value) {
        if(variableName != null) {
            VariableEntity variable = null;
            if(value instanceof VariableEntity) {
                variable = (VariableEntity) value;
            } else {
                variable = VariableMapper.toEntity(variableName, value);
            }
            variables.put(variableName, variable);
        }
    }

    @Override
    public void removeVariable(String variableName) {
        variables.remove(variableName);
    }

    @Override
    public Set<String> getVariableNames() {
        return variables.keySet();
    }

    @Override
    public boolean hasVariables() {
        return !variables.isEmpty();
    }

    @Override
    public boolean hasVariable(String variableName) {
        return variables.containsKey(variableName);
    }

    @Override
    public Map<String, Object> getVariables() {
        final Map<String, Object> result = new HashMap<>();
        if(variables != null && !variables.isEmpty()) {
            for(final String key : variables.keySet()) {
                final VariableEntity each = variables.get(key);
                Object value = null;
                if(each != null) {
                    value = VariableMapper.toInstance(each);
                }
                result.put(key, value);
            }
        }
        return result;
    }

    @Override
    public void setVariables(Map<String, Object> variables) {
        if (variables != null && !variables.isEmpty()) {
            for (final String variableName : variables.keySet()) {
                if (variableName != null) {
                    final Object each = variables.get(variableName);
                    VariableEntity variable = null;
                    if (each instanceof VariableEntity) {
                        variable = (VariableEntity) each;
                    } else {
                        variable = VariableMapper.toEntity(variableName, each);
                        variable.setName(variableName);
                        variable.setTokenId(this.getId());
                        variable.setProcessInstanceId(this.getProcessInstanceId());
                    }
                    this.variables.put(variableName, variable);
                }
            }
        }
    }

    public void addChildToken(Token token) {
        childTokens.add(token);
    }

    @Override
    public String getCurrentActivityId() {
        return currentActivity;
    }

    @Override
    public FlowElement getCurrentFlowElement() {
        if(currentFlowElement == null) {
            final Process process = ProcessDefinitionUtil.getProcess(getProcessDefinitionId());
            currentFlowElement = process.getFlowElement(getCurrentActivityId(), true);
        }
        return currentFlowElement;
    }

    @Override
    public void setCurrentFlowElement(FlowElement currentFlowElement) {
        this.currentFlowElement = currentFlowElement;
        if(currentFlowElement != null) {
            currentActivity = currentFlowElement.getId();
        } else {
            currentActivity = null;
        }
    }

    @Override
    public boolean isProcessInstanceType() {
        return parent == null;
    }

    /*---------- Getter/Setters ----------*/

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Token getParent() {
        return parent;
    }

    public void setParent(Token parent) {
        this.parent = parent;
    }

    public ProcessDefinitionEntity getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinitionEntity processDefinition) {
        this.processDefinition = processDefinition;
    }

    @Override
    public Long getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(Long rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    @Override
    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean isScope() {
        return isScope;
    }

    @Override
    public void setScope(boolean isScope) {
        this.isScope = isScope;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public List<Token> getChildTokens() {
        return childTokens;
    }

    public void setChildTokens(List<Token> childTokens) {
        this.childTokens = childTokens;
    }

    public List<EventSubscription> getEventSubscriptions() {
        return eventSubscriptions;
    }

    public void setEventSubscriptions(List<EventSubscription> eventSubscriptions) {
        this.eventSubscriptions = eventSubscriptions;
    }

    /*---------- Instance Getter/Setters ----------*/

    @Override
    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean isEnded) {
        this.isEnded = isEnded;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isEventScope() {
        return isEventScope;
    }

    public void setEventScope(boolean isEventScope) {
        this.isEventScope = isEventScope;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<JobEntity> getJobs() {
        if(jobs == null) {
            jobs = Context.getCommandContext().getJobService().findByTokenId(id);
        }
        return jobs;
    }

    public void setJobs(List<JobEntity> jobs) {
        this.jobs = jobs;
    }

    public Token getSubProcessInstance() {
        return subProcessInstance;
    }

    public void setSubProcessInstance(Token subProcessInstance) {
        this.subProcessInstance = subProcessInstance;
    }
}
