package com.kaaterskil.workflow.engine.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.bpm.foundation.Documentation;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.deploy.ParsedDeployment;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.util.FileUtil;
import com.kaaterskil.workflow.util.ValidationUtils;
import com.kaaterskil.workflow.util.XmlConverter;

public class BpmParser {
    private static final Logger log = LoggerFactory.getLogger(BpmParser.class);

    private final XmlConverter xmlConverter;
    private final DeploymentEntity deployment;
    private final String fileName;
    private String xml;
    private Process process;
    private ProcessDefinitionEntity processDefinition;
    private BpmParseHelper parseHelper;

    public BpmParser(DeploymentEntity deployment) {
        this.deployment = deployment;
        this.fileName = deployment.getName();
        this.xmlConverter = XmlConverter.getInstance();
        this.parseHelper = Context.getProcessEngineService().getBpmParseHelper();
    }

    public ParsedDeployment execute() {
        try {
            readFile();
            parseProcess();
            createProcessDefinition();

            log.debug("Parsing fileName {} completed", fileName);
            return new ParsedDeployment(deployment, processDefinition, process, xml);

        } catch (final XmlMappingException e) {
            log.error("Invalid xm mapping for fileName {}: {}", fileName, e.getMessage());
            throw new WorkflowException("Invalid xml mapping: " + e.getMessage());
        } catch (final IOException e) {
            log.error("Error reading fileName {}", fileName);
            throw new WorkflowException("Error reading fileName: " + fileName);
        }
    }

    private void readFile() throws IOException {
        if (ValidationUtils.isEmptyOrWhitespace(fileName)) {
            throw new IllegalArgumentException("Deployment fileName must not be empty");
        }
        log.debug("Reading deployment file {}", fileName);

        xml = FileUtil.readFileFromClasspath(fileName);
    }

    private void parseProcess() throws XmlMappingException, IOException {
        log.debug("Parsing deployment file {}", fileName);
        process = (Process) xmlConverter.read(fileName);
        parseHelper.parseElement(this, process);
    }

    public void processFlowElements(List<FlowElement> flowElements) {
        final List<SequenceFlow> sequenceFlows = new ArrayList<>();

        for(final FlowElement flowElement : flowElements) {
            if(flowElement instanceof SequenceFlow) {
                sequenceFlows.add((SequenceFlow) flowElement);
            }
        }
        for(final SequenceFlow sequenceFlow : sequenceFlows) {
            parseHelper.parseElement(this, sequenceFlow);
        }
    }

    private void createProcessDefinition() {
        log.debug("Creating process definition from parsed process");

        final ProcessDefinitionEntity entity = Context.getCommandContext()
                .getProcessDefinitionService().create();
        entity.setKey(process.getId());
        entity.setName(process.getName());
        entity.setDescription(formatDocumentation(process));
        entity.setDeploymentId(deployment.getId());

        processDefinition = entity;
    }

    private String formatDocumentation(Process process) {
        final List<Documentation> documentation = process.getDocumentation();
        if (documentation != null && !documentation.isEmpty()) {
            final StringBuffer sb = new StringBuffer();
            boolean first = true;
            for (final Documentation each : documentation) {
                if(!first) {
                    sb.append(" ");
                }
                sb.append(each.getText());
                first = false;
            }
            return sb.toString();
        }
        return null;
    }

    /*---------- Getter/Setters ----------*/

    public String getXml() {
        return xml;
    }

    public Process getProcess() {
        return process;
    }

    public ProcessDefinitionEntity getProcessDefinition() {
        return processDefinition;
    }

    public BpmParseHelper getParseHelper() {
        return parseHelper;
    }

    public void setParseHelper(BpmParseHelper parseHelper) {
        this.parseHelper = parseHelper;
    }

}
