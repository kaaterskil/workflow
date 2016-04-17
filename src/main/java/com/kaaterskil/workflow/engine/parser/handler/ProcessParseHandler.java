package com.kaaterskil.workflow.engine.parser.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.ImplementationType;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.bpm.foundation.Documentation;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.parser.BpmParser;
import com.kaaterskil.workflow.engine.parser.ListenerFactory;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;

public class ProcessParseHandler extends AbstractParseHandler<Process> {
    private static final Logger log = LoggerFactory.getLogger(ProcessParseHandler.class);

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return Process.class;
    }

    @Override
    protected void executeParse(BpmParser parser, Process process) {
        transformProcess(parser, process);
    }

    private void transformProcess(BpmParser parser, Process process) {
        final ProcessDefinitionEntity processDefinition = createProcessDefinition(parser, process);

        createEventListeners(process.getExtensionElements(), processDefinition);

        parser.processFlowElements(process.getFlowElements());

        parser.setProcessDefinition(processDefinition);
    }

    private ProcessDefinitionEntity createProcessDefinition(BpmParser parser, Process process) {
        log.debug("Creating process Definition");
        final ProcessDefinitionEntity entity = Context.getCommandContext()
                .getProcessDefinitionService().create();
        entity.setKey(process.getId());
        entity.setName(process.getName());
        entity.setDescription(formatDocumentation(process));
        entity.setDeploymentId(parser.getDeployment().getId());
        return entity;
    }

    private String formatDocumentation(Process process) {
        final List<Documentation> documentation = process.getDocumentation();
        if (documentation != null && !documentation.isEmpty()) {
            final StringBuffer sb = new StringBuffer();
            boolean first = true;
            for (final Documentation each : documentation) {
                if (!first) {
                    sb.append(" ");
                }
                sb.append(each.getText());
                first = false;
            }
            return sb.toString();
        }
        return null;
    }

    private void createEventListeners(List<Object> extensionElements,
            ProcessDefinitionEntity processDefinition) {
        final List<Listener> listeners = new ArrayList<>();
        if (extensionElements != null && !extensionElements.isEmpty()) {
            log.debug("Creating event listeners");
            for (final Object element : extensionElements) {
                if (element instanceof Listener) {
                    listeners.add((Listener) element);
                }
            }
        }

        final ListenerFactory listenerFactory = Context.getProcessEngineService()
                .getListenerFactory();
        for (final Listener listener : listeners) {
            final WorkflowEventType[] eventTypes = WorkflowEventType
                    .parseTypes(listener.getEventRefs());

            final ImplementationType implementationType = listener.getImplementationType();
            if (implementationType.equals(ImplementationType.CLASS)) {
                final WorkflowEventListener eventListener = listenerFactory
                        .createClassDelegateEventListener(listener);
                processDefinition.getEventHelper().addEventListener(eventListener, eventTypes);
            }
        }
    }

}
