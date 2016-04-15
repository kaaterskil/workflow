package com.kaaterskil.workflow.engine.behavior;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.gateway.ExclusiveGateway;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class ExclusiveGatewayActivityBehavior extends GatewayActivityBehavior {
    private static final Logger log = LoggerFactory
            .getLogger(ExclusiveGatewayActivityBehavior.class);

    @Override
    public void leave(DelegateToken token) {
        log.debug("Leaving exclusive gateway {}", token.getCurrentActivityId());

        final ExclusiveGateway exclusiveGateway = (ExclusiveGateway) token.getCurrentFlowElement();

        Context.getProcessEngineService().getEventDispatcher()
                .dispatchEvent(WorkflowEventFactory.createActivityEvent(
                        WorkflowEventType.ACTIVITY_COMPLETED, exclusiveGateway.getId(),
                        exclusiveGateway.getName(), (Token) token, exclusiveGateway));

        SequenceFlow outgoingSequenceFlow = null;
        SequenceFlow defaultSequenceFlow = null;
        final String defaultSequenceFlowId = exclusiveGateway.getDefaultSequenceFlow();

        final Iterator<SequenceFlow> iter = exclusiveGateway.getOutgoing().iterator();
        while (outgoingSequenceFlow == null && iter.hasNext()) {
            final SequenceFlow sequenceFlow = iter.next();
            outgoingSequenceFlow = sequenceFlow;

            if (defaultSequenceFlowId != null
                    && defaultSequenceFlowId.equals(sequenceFlow.getId())) {
                defaultSequenceFlow = sequenceFlow;
            }
        }

        if (outgoingSequenceFlow != null) {
            token.setCurrentFlowElement(outgoingSequenceFlow);
        } else {
            if (defaultSequenceFlow != null) {
                token.setCurrentFlowElement(defaultSequenceFlow);
            } else {
                throw new WorkflowException("No outgoing sequence flow for exclusive gateway "
                        + exclusiveGateway.getName());
            }
        }

        super.leave(token);
    }

}
