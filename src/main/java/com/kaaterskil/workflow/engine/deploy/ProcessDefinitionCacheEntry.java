package com.kaaterskil.workflow.engine.deploy;

import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.parser.BpmModel;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

public class ProcessDefinitionCacheEntry {

    private ProcessDefinitionEntity processDefinitionEntity;
    private BpmModel bpmModel;
    private Process process;
    private String xml;

    public ProcessDefinitionCacheEntry(ProcessDefinitionEntity processDefinitionEntity,
            BpmModel bpmModel, Process process, String xml) {
        this.processDefinitionEntity = processDefinitionEntity;
        this.bpmModel = bpmModel;
        this.process = process;
        this.xml = xml;
    }

    public BpmModel getBpmModel() {
        return bpmModel;
    }

    public void setBpmModel(BpmModel bpmModel) {
        this.bpmModel = bpmModel;
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
