package com.kaaterskil.workflow.engine.behavior;

import java.util.Iterator;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.util.tree.TokenTree;
import com.kaaterskil.workflow.engine.util.tree.TokenTreeNode;

public class TerminateEndEventActivityBehavior extends FlowNodeActivityBehavior {

    @Override
    public void execute(DelegateToken token) {
        final CommandContext commandContext = Context.getCommandContext();
        final TokenService tokenService = commandContext.getTokenService();

        final TokenTree tokenTree = tokenService.findTokenTree(token.getProcessInstanceId());
        deleteTokens(commandContext, tokenService, tokenTree.firstIterator());
    }

    private void deleteTokens(CommandContext commandContext, TokenService tokenService,
            Iterator<TokenTreeNode> iterator) {
        while (iterator.hasNext()) {
            final TokenTreeNode node = iterator.next();
            final Token token = node.getToken();
            tokenService.deleteTokenAndRelatedData(token, null);
        }
    }
}
