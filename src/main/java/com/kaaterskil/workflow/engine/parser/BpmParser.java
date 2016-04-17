package com.kaaterskil.workflow.engine.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;

import com.kaaterskil.workflow.bpm.ImplementationType;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.deploy.ParsedDeployment;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.DeploymentEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;
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

        for (final FlowElement flowElement : flowElements) {
            if (flowElement instanceof SequenceFlow) {
                sequenceFlows.add((SequenceFlow) flowElement);
            }
            processTokenListeners(flowElement);
        }
        for (final SequenceFlow sequenceFlow : sequenceFlows) {
            parseHelper.parseElement(this, sequenceFlow);
        }
    }

    private void processTokenListeners(FlowElement flowElement) {
        final List<Listener> listeners = flowElement.getTokenListeners();

        if (listeners != null && !listeners.isEmpty()) {
            for (final Listener listener : listeners) {
                final String beanName = listener.getImplementation();

                if (!ValidationUtils.isEmptyOrWhitespace(beanName)
                        && listener.getImplementationType().equals(ImplementationType.CLASS)) {
                    final TokenListener tokenListener = ApplicationContextUtil.instantiate(beanName,
                            TokenListener.class);
                    listener.setInstance(tokenListener);
                }
            }
        }
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

    public void setProcessDefinition(ProcessDefinitionEntity processDefinition) {
        this.processDefinition = processDefinition;
    }

    public BpmParseHelper getParseHelper() {
        return parseHelper;
    }

    public void setParseHelper(BpmParseHelper parseHelper) {
        this.parseHelper = parseHelper;
    }

    public DeploymentEntity getDeployment() {
        return deployment;
    }

}
