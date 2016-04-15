package com.kaaterskil.workflow.engine.deploy;

import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

public class ProcessDefinitionCacheEntry {

    protected ProcessDefinitionEntity processDefinitionEntity;
    protected Process process;
    protected String xml;

    public ProcessDefinitionCacheEntry(ProcessDefinitionEntity processDefinitionEntity,
            Process process, String xml) {
        this.processDefinitionEntity = processDefinitionEntity;
        this.process = process;
        this.xml = xml;
    }

    public ProcessDefinitionEntity getProcessDefinitionEntity() {
        return processDefinitionEntity;
    }

    public void setProcessDefinitionEntity(ProcessDefinitionEntity processDefinitionEntity) {
        this.processDefinitionEntity = processDefinitionEntity;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
