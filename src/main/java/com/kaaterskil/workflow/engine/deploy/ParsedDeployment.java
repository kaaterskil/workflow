package com.kaaterskil.workflow.engine.deploy;

import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

public class ParsedDeployment {

    private final DeploymentEntity deployment;
    private final ProcessDefinitionEntity processDefinition;
    private final Process process;
    private final String xml;

    public ParsedDeployment(DeploymentEntity deployment, ProcessDefinitionEntity processDefinition,
            Process process, String xml) {
        this.deployment = deployment;
        this.processDefinition = processDefinition;
        this.process = process;
        this.xml = xml;
    }

    public DeploymentEntity getDeployment() {
        return deployment;
    }

    public ProcessDefinitionEntity getProcessDefinition() {
        return processDefinition;
    }

    public Process getProcess() {
        return process;
    }

    public String getXml() {
        return xml;
    }
}
