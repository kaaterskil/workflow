package com.kaaterskil.workflow.engine.behavior;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.gateway.ParallelGateway;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;

public class ParallelGatewayActivityBehavior extends GatewayActivityBehavior {
    private static final Logger log = LoggerFactory
            .getLogger(ParallelGatewayActivityBehavior.class);

    @Override
    public void execute(DelegateToken token) {

        token.setActive(false);

        final FlowElement flowElement = token.getCurrentFlowElement();
        ParallelGateway parallelGateway = null;
        if (flowElement instanceof ParallelGateway) {
            parallelGateway = (ParallelGateway) flowElement;
        } else {
            throw new WorkflowException("ParallelGateway behavior cannot be applied");
        }

        lockFirstParentScope(token);

        DelegateToken multiInstanceExecution = null;
        if (hasMultiInstanceParent(parallelGateway)) {
            multiInstanceExecution = findMultiInstanceParentExecution(token);
        }

        final TokenService tokenService = Context.getCommandContext().getTokenService();
        List<Token> joinedExecutions = tokenService
                .findInactiveTokensByActivityIdAndProcessInstanceId(token.getCurrentActivityId(),
                        token.getProcessInstanceId());
        if (multiInstanceExecution != null) {
            joinedExecutions = cleanJoinedExecutions(joinedExecutions, multiInstanceExecution);
        }

        Context.getCommandContext().getActivityService().recordActivityEnd((Token) token);

        final int numToJoin = parallelGateway.getIncoming().size();
        final int numCurrentlyJoined = joinedExecutions.size();
        if (numCurrentlyJoined == numToJoin) {
            log.debug("Parallel gateway {} activates: {} of {} joined",
                    token.getCurrentActivityId(), numCurrentlyJoined, numToJoin);

            if (parallelGateway.getIncoming().size() > 1) {
                // All now inactive children are deleted
                for (final Token joinedExecution : joinedExecutions) {
                    // The current token will be reused and not deleted
                    if (!joinedExecution.getId().equals(token.getId())) {
                        Context.getCommandContext().getTokenService()
                                .deleteTokenAndRelatedData(joinedExecution, null);
                    }
                }
            }
            Context.getCommandContext().getWorkflow().addOutgoingSequenceFlow((Token) token, false);
        } else {
            log.debug("Parallel gateway {} does not activate: {} of {} joined",
                    token.getCurrentActivityId(), numCurrentlyJoined, numToJoin);
        }
    }

    protected List<Token> cleanJoinedExecutions(List<Token> joinedExecutions,
            DelegateToken multiInstanceExecution) {
        final List<Token> cleanedExecutions = new ArrayList<>();
        for (final Token each : joinedExecutions) {
            if (isChildOfMultiInstanceExecution(each, multiInstanceExecution)) {
                cleanedExecutions.add(each);
            }
        }
        return cleanedExecutions;
    }

    protected boolean isChildOfMultiInstanceExecution(DelegateToken executionEntity,
            DelegateToken multiInstanceExecution) {
        boolean isChild = false;
        final DelegateToken parentExecution = executionEntity.getParent();
        if (parentExecution != null) {
            if (parentExecution.getId().equals(multiInstanceExecution.getId())) {
                isChild = true;
            } else {
                final boolean isNestedChild = isChildOfMultiInstanceExecution(parentExecution,
                        multiInstanceExecution);
                if (isNestedChild) {
                    isChild = true;
                }
            }
        }
        return isChild;
    }

    protected boolean hasMultiInstanceParent(FlowNode flowNode) {
        boolean hasMultiInstanceParent = false;

        if (flowNode.getSubProcess() != null) {
            if (flowNode.getSubProcess().getLoopCharacteristics() != null) {
                hasMultiInstanceParent = true;
            } else {
                final boolean hasNestedMultiInstanceParent = hasMultiInstanceParent(
                        flowNode.getSubProcess());
                if (hasNestedMultiInstanceParent) {
                    hasMultiInstanceParent = true;
                }
            }
        }
        return hasMultiInstanceParent;
    }

    protected DelegateToken findMultiInstanceParentExecution(DelegateToken token) {
        DelegateToken multiInstanceExecution = null;
        final DelegateToken parentExecution = token.getParent();
        if (parentExecution != null && parentExecution.getCurrentFlowElement() != null) {
            final FlowElement flowElement = parentExecution.getCurrentFlowElement();
            if (flowElement instanceof Activity) {
                final Activity activity = (Activity) flowElement;
                if (activity.getLoopCharacteristics() != null) {
                    multiInstanceExecution = parentExecution;
                }
            }

            if (multiInstanceExecution == null) {
                final DelegateToken target = findMultiInstanceParentExecution(parentExecution);
                if (target != null) {
                    multiInstanceExecution = target;
                }
            }
        }
        return multiInstanceExecution;
    }

}
