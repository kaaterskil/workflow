package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public abstract class GatewayActivityBehavior extends FlowNodeActivityBehavior {

    protected void lockFirstParentScope(DelegateToken token) {
        boolean found = false;
        Token parentScopeToken = null;
        Token currentToken = (Token) token;
        while(!found && currentToken != null && currentToken.getParent() != null) {
            parentScopeToken = currentToken.getParent();
            if(parentScopeToken != null && parentScopeToken.isScope()) {
                found = true;
            }
            currentToken = parentScopeToken;
        }

        parentScopeToken.forceUpdate();
    }
}
