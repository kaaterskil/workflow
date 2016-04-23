package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.event.StartEvent;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.util.CollectionUtil;

public class SubProcessActivityBehavior extends BaseActivityBehavior {

    @Override
    public void execute(DelegateToken token) {
        final SubProcess subProcess = getSubProcessFromToken(token);

        FlowElement startElement = null;
        if (CollectionUtil.isNotEmpty(subProcess.getFlowElements())) {
            for (final FlowElement each : subProcess.getFlowElements()) {
                if (each instanceof StartEvent) {
                    final StartEvent startEvent = (StartEvent) each;

                    if (CollectionUtil.isNotEmpty(startEvent.getEventDefinitions())) {
                        startElement = startEvent;
                        break;
                    }
                }
            }
        }
        if (startElement == null) {
            throw new WorkflowException(
                    "No initial activity found for subproess " + subProcess.getId());
        }

        token.setScope(true);

        // TODO handle data objects

        final Token startSubProcessToken = Context.getCommandContext().getTokenService()
                .createChildToken((Token) token);
        startSubProcessToken.setCurrentFlowElement(startElement);
        Context.getWorkflow().addContinueProcessOperation(startSubProcessToken);
    }

    private SubProcess getSubProcessFromToken(DelegateToken token) {
        final FlowElement flowElement = token.getCurrentFlowElement();

        SubProcess subProcess = null;
        if (flowElement instanceof SubProcess) {
            subProcess = (SubProcess) flowElement;
        } else {
            throw new WorkflowException("Subprocess behavior can only be applied to a sub process");
        }
        return subProcess;
    }
}
