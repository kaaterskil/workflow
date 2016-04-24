package com.kaaterskil.workflow.engine.behavior;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.event.IntermediateCatchEvent;
import com.kaaterskil.workflow.bpm.common.gateway.EventBasedGateway;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.util.CollectionUtil;

public class IntermediateCatchEventActivityBehavior extends BaseActivityBehavior {

    @Override
    public void execute(DelegateToken token) {
        // Do nothing - wait state
    }

    @Override
    public void trigger(DelegateToken token, String signalEvent, Object signalData) {
        leaveIntermediateCatchEvent(token);
    }

    public void leaveIntermediateCatchEvent(DelegateToken token) {
        final EventBasedGateway gateway = getPrecedingEventBasedGateway(token);
        if (gateway != null) {
            deleteOtherEventsRelatedToEventBasedGateway(token, gateway);
        }

        leave(token);
    }

    public void cancelEvent(DelegateToken token) {
        Context.getCommandContext().getTokenService().deleteTokenAndRelatedData((Token) token,
                null);
    }

    private EventBasedGateway getPrecedingEventBasedGateway(DelegateToken token) {
        final FlowElement flowElement = token.getCurrentFlowElement();
        if (flowElement != null && flowElement instanceof IntermediateCatchEvent) {
            final IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) flowElement;

            final List<SequenceFlow> incoming = catchEvent.getIncoming();
            if (incoming != null && incoming.size() == 1) {
                final SequenceFlow sequenceFlow = incoming.get(0);
                final FlowElement sourceFlowElement = sequenceFlow.getSourceFlowElement();
                if (sourceFlowElement instanceof EventBasedGateway) {
                    return (EventBasedGateway) sourceFlowElement;
                }
            }
        }
        return null;
    }

    private void deleteOtherEventsRelatedToEventBasedGateway(DelegateToken token,
            EventBasedGateway gateway) {
        final List<SequenceFlow> outgoing = gateway.getOutgoing();

        final Set<String> eventActivityIds = new HashSet<>();
        if (CollectionUtil.isNotEmpty(outgoing)) {
            for (final SequenceFlow outgoingSequenceFlow : outgoing) {
                final FlowElement targetFlowElement = outgoingSequenceFlow.getTargetFlowElement();
                if (targetFlowElement != null
                        && !targetFlowElement.getId().equals(token.getCurrentActivityId())) {
                    eventActivityIds.add(targetFlowElement.getId());
                }
            }
        }

        final CommandContext commandContext = Context.getCommandContext();
        final TokenService tokenService = commandContext.getTokenService();

        final List<Token> tokens = tokenService.findByParentAndActivityIds(token.getParentId(),
                eventActivityIds);
        for (final Token each : tokens) {
            if (eventActivityIds.contains(each.getActivityId())
                    && token.getCurrentFlowElement() instanceof IntermediateCatchEvent) {
                final IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) token
                        .getCurrentFlowElement();

                if (catchEvent.getBehavior()
                        .equals(IntermediateCatchEventActivityBehavior.class.getCanonicalName())) {
                    final BehaviorHelper helper = Context.getProcessEngineService()
                            .getBehaviorHelper();
                    final IntermediateCatchEventActivityBehavior behavior = (IntermediateCatchEventActivityBehavior) helper
                            .getBehavior(catchEvent);
                    behavior.cancelEvent(each);
                    eventActivityIds.remove(each.getActivityId());
                }
            }
        }
    }
}
