package com.kaaterskil.workflow.engine.bpm;

import java.util.List;

import com.kaaterskil.workflow.engine.behavior.BaseActivityBehavior;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.SubProcessActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.delegate.invocation.TokenListenerInvocation;
import com.kaaterskil.workflow.engine.parser.FieldDeclaration;

public class ClassDelegate extends BaseActivityBehavior
        implements TokenListener, SubProcessActivityBehavior {

    private final String className;
    private TokenListener tokenListenerInstance;
    private ActivityBehavior behaviorInstance;
    private final List<FieldDeclaration> fieldDeclarations;

    public ClassDelegate(String className, List<FieldDeclaration> fieldDeclarations) {
        this.className = className;
        this.fieldDeclarations = fieldDeclarations;
    }

    @Override
    public void completing(DelegateToken token, DelegateToken subProcessToken) throws Exception {
        if (behaviorInstance == null) {
            behaviorInstance = getBehaviorInstance(token);
        }

        if (behaviorInstance instanceof SubProcessActivityBehavior) {
            ((SubProcessActivityBehavior) behaviorInstance).completing(token, subProcessToken);
        } else {
            final String message = String.format(
                    "bean {} does not implement SubProcessActivityBehavior",
                    behaviorInstance.getClass().getName());
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public void completed(DelegateToken token) throws Exception {
        if (behaviorInstance == null) {
            behaviorInstance = getBehaviorInstance(token);
        }

        if (behaviorInstance instanceof SubProcessActivityBehavior) {
            ((SubProcessActivityBehavior) behaviorInstance).completed(token);
        } else {
            final String message = String.format(
                    "bean {} does not implement SubProcessActivityBehavior",
                    behaviorInstance.getClass().getName());
            throw new IllegalArgumentException(message);
        }
    }

    private ActivityBehavior getBehaviorInstance(DelegateToken token) {
        final Object delegateInstance = instantiateDelegate(className, fieldDeclarations);
        if (delegateInstance instanceof ActivityBehavior) {
            return determineBehavior((ActivityBehavior) delegateInstance, token);
        } else {
            final String message = String.format("{} does not implement ActivityBehavior",
                    delegateInstance.getClass().getName());
            throw new IllegalArgumentException(message);
        }
    }

    private ActivityBehavior determineBehavior(ActivityBehavior delegateInstance,
            DelegateToken token) {
        if(multiInstanceActivityBehavior != null) {
            multiInstanceActivityBehavior.setInnerActivityBehavior((BaseActivityBehavior) delegateInstance);
            return multiInstanceActivityBehavior;
        }
        return delegateInstance;
    }

    @Override
    public void notify(DelegateToken token) {
        if (tokenListenerInstance == null) {
            tokenListenerInstance = getTokenListenerInstance();
        }
        Context.getProcessEngineService().getDelegateInterceptor()
                .handleInvocation(new TokenListenerInvocation(tokenListenerInstance, token));
    }

    public TokenListener getTokenListenerInstance() {
        final Object delegateInstance = instantiateDelegate(className, fieldDeclarations);
        if (delegateInstance instanceof TokenListener) {
            return (TokenListener) delegateInstance;
        } else {
            final String message = String.format("{} does not implement TokenListener",
                    delegateInstance.getClass().getName());
            throw new IllegalArgumentException(message);
        }
    }

    private Object instantiateDelegate(String className, List<FieldDeclaration> fieldDeclarations) {
        return ClassDelegateUtil.createDelegate(className, fieldDeclarations);
    }

}
