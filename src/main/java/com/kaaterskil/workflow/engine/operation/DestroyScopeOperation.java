package com.kaaterskil.workflow.engine.operation;

import java.util.List;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.persistence.entity.VariableEntity;
import com.kaaterskil.workflow.engine.service.JobService;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.service.VariableService;

public class DestroyScopeOperation extends AbstractOperation {

    public DestroyScopeOperation(CommandContext commandContext, Token token) {
        super(commandContext, token);
    }

    @Override
    public void run() {
        final FlowElement currentFlowElement = token.getCurrentFlowElement();
        final TokenService tokenService = commandContext.getTokenService();

        // Find the ancestor scope token
        final Token executionEntity = token;
        Token parentScopeToken = null;
        if (token.isScope()) {
            parentScopeToken = executionEntity;
        } else {
            Token target = token.getParent();
            while (target != null && parentScopeToken == null) {
                if (target.isScope()) {
                    parentScopeToken = target;
                } else {
                    target = target.getParent();
                }
            }
        }

        if (parentScopeToken == null) {
            throw new WorkflowException("Error: No parent scope token found for boundary event");
        }

        // Delete all child tokens
        final List<Token> children = tokenService
                .findChildTokensByParentTokenId(parentScopeToken.getId());
        for (final Token each : children) {
            tokenService.deleteTokenAndRelatedData(each, null);
        }

        // Delete all scope jobs
        final JobService jobService = Context.getCommandContext().getJobService();
        for (final JobEntity job : parentScopeToken.getJobs()) {
            parentScopeToken.getJobs().remove(job);
            jobService.delete(job);
        }

        // Remove variables associated with the scope
        final VariableService variableService = Context.getCommandContext().getVariableService();
        for (final String key : parentScopeToken.getVariables().keySet()) {
            parentScopeToken.getVariables().remove(key);
            final VariableEntity variable = variableService.findByName(key);
            variableService.delete(variable);
        }

        parentScopeToken.setScope(false);
        parentScopeToken.setCurrentFlowElement(currentFlowElement);
    }

}
