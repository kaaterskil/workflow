package com.kaaterskil.workflow.engine.behavior;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.util.TokenUtil;

public class InclusiveGatewayActivityBehavior extends GatewayActivityBehavior {
    private static final Logger log = LoggerFactory
            .getLogger(InclusiveGatewayActivityBehavior.class);

    /**
     * For join: When an token enters the gateway, it is inactivated. All the inactivated tokens
     * stay in the gateway until all tokens that can reach the gateway have reached it.
     */
    @Override
    public void execute(DelegateToken token) {
        token.setActive(false);
        executeBehavior((Token) token);
    }

    public void executeInactive(Token token) {
        executeBehavior(token);
    }

    protected void executeBehavior(Token token) {
        final CommandContext commandContext = Context.getCommandContext();
        final TokenService tokenService = commandContext.getTokenService();

        lockFirstParentScope(token);

        boolean reachableByOneToken = false;
        final List<Token> allTokens = tokenService
                .findChildTokensByProcessInstanceId(token.getProcessInstanceId());
        final Iterator<Token> iter = allTokens.iterator();
        while (!reachableByOneToken && iter.hasNext()) {
            final Token each = iter.next();
            if (!each.getActivityId().equals(token.getCurrentActivityId())) {
                final boolean reachable = TokenUtil.isReachable(token.getProcessDefinitionId(),
                        each.getActivityId(), token.getCurrentActivityId());
                if (reachable) {
                    reachableByOneToken = true;
                }
            } else
                if (each.getActivityId().equals(token.getCurrentActivityId()) && each.isActive()) {
                reachableByOneToken = true;
            }
        }

        // If no token can reach the gateway, the gateway activates and executes the fork behavior
        if (!reachableByOneToken) {
            log.debug("Inclusive gateway cannot be reached by any token and is activated");

            final List<Token> tokensInGateway = tokenService
                    .findInactiveTokensByActivityId(token.getCurrentActivityId());
            for (final Token gatewayToken : tokensInGateway) {
                if (!gatewayToken.getId().equals(token.getId())) {
                    tokenService.delete(gatewayToken);
                }
            }
            commandContext.getWorkflow().addOutgoingSequenceFlow(token, true);
        }
    }
}
