package com.kaaterskil.workflow.engine.behavior;

import java.util.ArrayList;
import java.util.List;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.event.CompensateEventDefinition;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;

public class BaseActivityBehavior extends FlowNodeActivityBehavior {

    protected MultiInstanceActivityBehavior multiInstanceActivityBehavior;

    @Override
    public void leave(DelegateToken token) {
        final FlowElement currentFlowElement = token.getCurrentFlowElement();
        final List<BoundaryEvent> boundaryEvents = findBoundaryEventsForFlowNode(
                token.getProcessDefinitionId(), currentFlowElement);
        if (!boundaryEvents.isEmpty()) {
            executeCompensateBoundaryEvents(boundaryEvents, token);
        }
        super.leave(token);
    }

    protected List<BoundaryEvent> findBoundaryEventsForFlowNode(Long processDefinitionId,
            FlowElement flowElement) {
        final Process process = getProcessDefinition(processDefinitionId);

        final List<BoundaryEvent> result = new ArrayList<>();
        final List<BoundaryEvent> boundaryEvents = process.findFlowElements(BoundaryEvent.class);
        for (final BoundaryEvent each : boundaryEvents) {
            if (each.getAttachedToRef() != null && each.getAttachedToRef().equals(flowElement.getId())) {
                result.add(each);
            }
        }
        return result;
    }

    protected void executeCompensateBoundaryEvents(List<BoundaryEvent> boundaryEvents,
            DelegateToken token) {
        for (final BoundaryEvent boundaryEvent : boundaryEvents) {
            if (boundaryEvent.getEventDefinitions().isEmpty()) {
                continue;
            }

            if (!(boundaryEvent.getEventDefinitions()
                    .get(0) instanceof CompensateEventDefinition)) {
                continue;
            }

            final Token childToken = Context.getCommandContext().getTokenService()
                    .createChildToken((Token) token);
            childToken.setParent((Token) token);
            childToken.setCurrentFlowElement(boundaryEvent);
            childToken.setScope(false);

            final ActivityBehavior boundaryEventBehavior = (ActivityBehavior) ApplicationContextUtil
                    .getBean(boundaryEvent.getBehavior());
            boundaryEventBehavior.execute(childToken);
        }
    }

    private Process getProcessDefinition(Long processDefinitionId) {
        return ProcessDefinitionUtil.getProcess(processDefinitionId);
    }

    /*---------- Getter/Setters ----------*/

    public MultiInstanceActivityBehavior getMultiInstanceActivityBehavior() {
        return multiInstanceActivityBehavior;
    }

    public void setMultiInstanceActivityBehavior(
            MultiInstanceActivityBehavior multiInstanceActivityBehavior) {
        this.multiInstanceActivityBehavior = multiInstanceActivityBehavior;
    }
}
