package com.kaaterskil.workflow.engine.behavior;

import java.util.List;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.artifact.Association;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.event.CompensateEventDefinition;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.CompensateEventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.EventSubscriptionService;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;

public class BoundaryCompensateEventActivityBehavior extends BoundaryEventActivityBehavior {

    private final CompensateEventDefinition compensateEventDefinition;

    public BoundaryCompensateEventActivityBehavior(boolean interrupting,
            CompensateEventDefinition compensateEventDefinition) {
        super(interrupting);
        this.compensateEventDefinition = compensateEventDefinition;
    }

    /*---------- Methods ----------*/

    @Override
    public void execute(DelegateToken delegateToken) {
        final Token token = (Token) delegateToken;
        final BoundaryEvent boundaryEvent = (BoundaryEvent) token.getCurrentFlowElement();

        final Process process = ProcessDefinitionUtil.getProcess(token.getProcessDefinitionId());
        if (process == null) {
            throw new WorkflowException(
                    "Process model could not be found for id " + token.getProcessDefinitionId());
        }

        Activity compensationActivity = null;
        final List<Association> associations = process.findAssociationsWithSourceRef(process,
                boundaryEvent.getId());
        for (final Association each : associations) {
            final FlowElement targetElement = process.getFlowElement(each.getTargetRef(), true);
            if (targetElement != null && targetElement instanceof Activity) {
                final Activity activity = (Activity) targetElement;
                if (activity.isForCompensation()) {
                    compensationActivity = activity;
                    break;
                }
            }
        }
        if (compensationActivity == null) {
            throw new WorkflowException("Compensation activity could not be found");
        }

        Token scopeToken = null;
        Token parentToken = token.getParent();
        while (scopeToken == null && parentToken != null) {
            if (parentToken.getCurrentFlowElement() instanceof SubProcess) {
                scopeToken = parentToken;
            } else if (parentToken.getId().equals(parentToken.getProcessInstanceId())) {
                scopeToken = parentToken;
            } else {
                parentToken = parentToken.getParent();
            }
        }
        if (scopeToken == null) {
            throw new WorkflowException(
                    "Could not find a scoped token for boundary event " + boundaryEvent.getId());
        }

        final EventSubscriptionService subscriptionService = Context.getCommandContext()
                .getEventSubscriptionService();
        subscriptionService.insertCompensationEvent(scopeToken, compensationActivity.getId());
    }

    @Override
    public void trigger(DelegateToken delegateToken, String signalEvent, Object signalData) {
        final EventSubscriptionService eventSubscriptionService = Context.getCommandContext()
                .getEventSubscriptionService();
        final Token token = (Token) delegateToken;

        final BoundaryEvent boundaryEvent = (BoundaryEvent) token.getCurrentFlowElement();
        if (boundaryEvent.isCancelActivity()) {
            final List<EventSubscription> eventSubscriptions = token.getEventSubscriptions();
            for (final EventSubscription subscription : eventSubscriptions) {
                if (subscription instanceof CompensateEventSubscription && subscription
                        .getActivityId().equals(compensateEventDefinition.getActivityRef())) {
                    eventSubscriptionService.delete(subscription);
                }
            }
        }

        super.trigger(delegateToken, signalEvent, signalData);
    }
}
