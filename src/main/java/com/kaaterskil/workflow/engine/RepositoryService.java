package com.kaaterskil.workflow.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.command.CreateDeploymentCommand;
import com.kaaterskil.workflow.engine.command.DeployCommand;
import com.kaaterskil.workflow.engine.deploy.DeploymentFactory;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;
import com.kaaterskil.workflow.engine.service.ActivityService;
import com.kaaterskil.workflow.engine.service.DeploymentService;
import com.kaaterskil.workflow.engine.service.EventSubscriptionService;
import com.kaaterskil.workflow.engine.service.JobService;
import com.kaaterskil.workflow.engine.service.ProcessDefinitionService;
import com.kaaterskil.workflow.engine.service.ProcessInstanceService;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.service.VariableService;

@Component
public class RepositoryService extends AbstractService {

    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @Autowired
    private ProcessInstanceService processInstanceService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private VariableService variableService;

    @Autowired
    private JobService jobService;

    @Autowired
    private EventSubscriptionService eventSubscriptionService;

    /*---------- Methods ----------*/

    public DeploymentFactory createDeployment() {
        return commandExecutor.execute(new CreateDeploymentCommand());
    }

    public DeploymentEntity deploy(DeploymentFactory factory) {
        return commandExecutor.execute(new DeployCommand(factory));
    }

    /*---------- Getter/Setters ----------*/

    public DeploymentService getDeploymentService() {
        return deploymentService;
    }

    public void setDeploymentService(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    public ProcessDefinitionService getProcessDefinitionService() {
        return processDefinitionService;
    }

    public void setProcessDefinitionService(ProcessDefinitionService processDefinitionService) {
        this.processDefinitionService = processDefinitionService;
    }

    public ProcessInstanceService getProcessInstanceService() {
        return processInstanceService;
    }

    public void setProcessInstanceService(ProcessInstanceService processInstanceService) {
        this.processInstanceService = processInstanceService;
    }

    public ActivityService getActivityService() {
        return activityService;
    }

    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public VariableService getVariableService() {
        return variableService;
    }

    public void setVariableService(VariableService variableService) {
        this.variableService = variableService;
    }

    public JobService getJobService() {
        return jobService;
    }

    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    public EventSubscriptionService getEventSubscriptionService() {
        return eventSubscriptionService;
    }

    public void setEventSubscriptionService(EventSubscriptionService eventSubscriptionService) {
        this.eventSubscriptionService = eventSubscriptionService;
    }
}
