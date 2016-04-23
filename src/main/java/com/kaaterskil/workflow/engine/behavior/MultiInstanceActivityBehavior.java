package com.kaaterskil.workflow.engine.behavior;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.ImplementationType;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.Expression;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.SubProcessActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.parser.factory.ListenerFactory;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.util.CollectionUtil;

public abstract class MultiInstanceActivityBehavior extends FlowNodeActivityBehavior
        implements SubProcessActivityBehavior {
    private static final Logger log = LoggerFactory.getLogger(MultiInstanceActivityBehavior.class);

    protected final String NUMBER_OFINSTANCES = "numInstances";
    protected final String NUMBER_OF_ACTIVE_INSTANCES = "numActiveInstances";
    protected final String NUMBER_OF_COMPLETED_INSTANCES = "numCompletedInstances";
    protected final String DEFAULT_COLLECTION_ELEMENT_KEY = "loopCounter";

    protected Activity activity;
    protected BaseActivityBehavior innerActivityBehavior;
    protected Expression loopCardinalityExpression;
    protected Expression completionConditionExpression;

    protected Expression collectionExpression;
    protected String collectionVariable;
    protected String collectionElementVariable;
    protected String loopCounterKey = DEFAULT_COLLECTION_ELEMENT_KEY;

    public MultiInstanceActivityBehavior(Activity activity,
            BaseActivityBehavior innerActivityBehavior) {
        this.activity = activity;
        this.innerActivityBehavior = innerActivityBehavior;
    }

    @Override
    public void execute(DelegateToken token) {
        if (getLoopVariable(token, "loopCounter") == null) {
            int numInstances = 0;

            try {
                numInstances = createInstances(token);
            } catch (final Exception e) {
                throw new WorkflowException("Error creating instances for MultiInstanceBehavior");
            }

            if (numInstances == 0) {
                super.leave(token);
            }
        } else {
            innerActivityBehavior.execute(token);
        }
    }

    @Override
    public void completing(DelegateToken token, DelegateToken subProcessToken) throws Exception {
        // Noop
    }

    @Override
    public void completed(DelegateToken token) throws Exception {
        leave(token);
    }

    @Override
    public void trigger(DelegateToken token, String signalEvent, Object signalData) {
        innerActivityBehavior.trigger(token, signalEvent, signalData);
    }

    /*---------- Helper Methods ----------*/

    protected abstract int createInstances(DelegateToken token);

    protected Integer getLoopVariable(DelegateToken token, String variableName) {
        Object value = token.getVariable(variableName);
        DelegateToken parent = token.getParent();

        while (value == null && parent != null) {
            value = parent.getVariable(variableName);
            parent = parent.getParent();
        }
        return (Integer) (value != null ? value : 0);
    }

    protected void setLoopVariable(DelegateToken token, String variableName, Object value) {
        token.setVariable(variableName, value);
    }

    protected void removeLoopVariable(DelegateToken token, String variableName) {
        token.removeVariable(variableName);
    }

    @SuppressWarnings("rawtypes")
    protected int resolveNumberOfInstances(DelegateToken token) {
        int numInstances = -1;

        if (loopCardinalityExpression != null) {
            numInstances = resolveLoopCardinality(token);
        } else if (collectionExpression != null) {
            // TODO get collection from expression (how?)
        } else if (collectionVariable != null) {
            final Object obj = token.getVariable(collectionVariable);
            if (obj == null) {
                throw new IllegalArgumentException(
                        "Variable " + collectionVariable + " could not be found");
            }
            if (!(obj instanceof Collection)) {
                throw new IllegalArgumentException(
                        "Variable " + collectionVariable + " is not a Collection");
            }
            numInstances = ((Collection) obj).size();
        } else {
            throw new IllegalArgumentException(
                    "Could not resolve collection expression or variable reference");
        }
        return numInstances;
    }

    protected int resolveLoopCardinality(DelegateToken token) {
        final String text = loopCardinalityExpression.getDocumentation().get(0).getText();
        if (text.matches("[0-9]+")) {
            return Integer.valueOf(text);
        }
        throw new IllegalArgumentException("Could not resolve loopCardinality expression");
    }

    @SuppressWarnings("rawtypes")
    protected void executeOriginalBehavior(DelegateToken token, int loopCounter) {
        if (usesCollection() && collectionElementVariable != null) {
            Collection collection = null;
            if (collectionExpression != null) {
                // TODO get collection from expression (how?)
            } else if (collectionVariable != null) {
                collection = (Collection) token.getVariable(collectionVariable);
            }

            Object value = null;
            int idx = 0;
            final Iterator iter = collection.iterator();
            while (idx <= loopCounter) {
                value = iter.next();
                idx++;
            }
            setLoopVariable(token, collectionElementVariable, value);
        }

        if (loopCounter == 0) {
            callCustomActivityStartListeners(token);
            innerActivityBehavior.execute(token);
        } else {
            token.setCurrentFlowElement(activity);
            Context.getWorkflow().addContinueMultiInstanceOperation((Token) token);
        }
    }

    protected boolean usesCollection() {
        return collectionExpression != null || collectionVariable != null;
    }

    protected void callCustomActivityStartListeners(DelegateToken token) {
        final List<Listener> listeners = activity.getTokenListeners();
        if (CollectionUtil.isNotEmpty(listeners)) {
            final ListenerFactory listenerFactory = Context.getProcessEngineService()
                    .getListenerFactory();

            for (final Listener listener : listeners) {
                if (TokenListener.EVENT_START.toString()
                        .equalsIgnoreCase(listener.getEventRefs())) {
                    TokenListener tokenListener = null;
                    if (ImplementationType.CLASS.equals(listener.getImplementationType())) {
                        tokenListener = listenerFactory.createClassDelegateTokenListener(listener);
                    } else
                        if (ImplementationType.INSTANCE.equals(listener.getImplementationType())) {
                        final Object targetListener = listener.getInstance();
                        if (targetListener instanceof TokenListener) {
                            tokenListener = (TokenListener) targetListener;
                        } else {
                            log.warn("Token litener target " + targetListener
                                    + " is not of type TokenListener");
                        }
                    }

                    if (tokenListener != null) {
                        ((Token) token).setEventName(TokenListener.EVENT_START);
                        tokenListener.notify(token);
                    }
                }
            }
        }
    }

    protected DelegateToken getMultiInstanceRootToken(DelegateToken token) {
        DelegateToken multiInstanceRootToken = null;
        DelegateToken currentToken = token;

        while (currentToken != null && multiInstanceRootToken == null
                && currentToken.getParent() != null) {
            if (currentToken.isMultiInstanceRoot()) {
                multiInstanceRootToken = currentToken;
            } else {
                currentToken = currentToken.getParent();
            }
        }
        return multiInstanceRootToken;
    }

    protected void callActivityEndListeners(DelegateToken token) {
        final List<Listener> listeners = activity.getTokenListeners();
        if (CollectionUtil.isNotEmpty(listeners)) {
            final ListenerFactory listenerFactory = Context.getProcessEngineService()
                    .getListenerFactory();

            for (final Listener listener : listeners) {
                if (TokenListener.EVENT_END.equalsIgnoreCase(listener.getEventRefs())) {
                    TokenListener tokenListener = null;
                    if (ImplementationType.CLASS.equals(listener.getImplementationType())) {
                        tokenListener = listenerFactory.createClassDelegateTokenListener(listener);
                    } else
                        if (ImplementationType.INSTANCE.equals(listener.getImplementationType())) {
                        final Object targetListener = listener.getInstance();
                        if (targetListener instanceof TokenListener) {
                            tokenListener = (TokenListener) targetListener;
                        } else {
                            log.warn("Token litener target " + targetListener
                                    + " is not of type TokenListener");
                        }
                    }
                    if (tokenListener != null) {
                        ((Token) token).setEventName(TokenListener.EVENT_END);
                        tokenListener.notify(token);
                    }
                }
            }
        }
    }

    protected boolean completionConditionSatisfied(DelegateToken token) {
        if (completionConditionExpression != null) {
            final String value = completionConditionExpression.getDocumentation().get(0).getText();
            if (value == null) {
                throw new IllegalArgumentException("Completion condition cannot be null");
            }
            final Boolean booleanValue = Boolean.valueOf(value);
            log.debug("Multi-instance completion condition satisfied: {}", booleanValue);
            return booleanValue;
        }
        return false;
    }

    /*---------- Getter/Setters ----------*/

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public BaseActivityBehavior getInnerActivityBehavior() {
        return innerActivityBehavior;
    }

    public void setInnerActivityBehavior(BaseActivityBehavior innerActivityBehavior) {
        this.innerActivityBehavior = innerActivityBehavior;
    }

    public Expression getLoopCardinalityExpression() {
        return loopCardinalityExpression;
    }

    public void setLoopCardinalityExpression(Expression loopCardinalityExpression) {
        this.loopCardinalityExpression = loopCardinalityExpression;
    }

    public Expression getCompletionConditionExpression() {
        return completionConditionExpression;
    }

    public void setCompletionConditionExpression(Expression completionConditionExpression) {
        this.completionConditionExpression = completionConditionExpression;
    }

    public Expression getCollectionExpression() {
        return collectionExpression;
    }

    public void setCollectionExpression(Expression collectionExpression) {
        this.collectionExpression = collectionExpression;
    }

    public String getCollectionVariable() {
        return collectionVariable;
    }

    public void setCollectionVariable(String collectionVariable) {
        this.collectionVariable = collectionVariable;
    }

    public String getCollectionElementVariable() {
        return collectionElementVariable;
    }

    public void setCollectionElementVariable(String collectionElementVariable) {
        this.collectionElementVariable = collectionElementVariable;
    }

    public String getLoopCounterKey() {
        return loopCounterKey;
    }

    public void setLoopCounterKey(String loopCounterKey) {
        this.loopCounterKey = loopCounterKey;
    }
}
