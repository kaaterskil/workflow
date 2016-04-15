package com.kaaterskil.workflow.engine.delegate.event;

import java.util.Map;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class WorkflowEventFactory {

    public static EntityWorkflowEvent createEntityEvent(WorkflowEventType type, Object entity) {
        final EntityWorkflowEvent event = new EntityWorkflowEvent(type, entity);
        setEventContext(event);
        return event;
    }

    public static EntityWithVariablesWorkflowEvent createEntityWithVariablesEvent(
            WorkflowEventType type, Object entity, Map<String, Object> variables) {
        final EntityWithVariablesWorkflowEvent event = new EntityWithVariablesWorkflowEvent(type,
                entity, variables);
        setEventContext(event);
        return event;
    }

    public EntityExceptionWorkflowEvent createEntityExceptionEvent(WorkflowEventType type,
            Object entity, Throwable cause) {
        final EntityExceptionWorkflowEvent event = new EntityExceptionWorkflowEvent(type, entity,
                cause);
        setEventContext(event);
        return event;
    }

    public static ProcessStartedWorkflowEvent createProcessStartedEvent(Object entity,
            Map<String, Object> variables) {
        final ProcessStartedWorkflowEvent event = new ProcessStartedWorkflowEvent(entity,
                variables);
        setEventContext(event);
        return event;
    }

    public static SequenceFlowTakenWorkflowEvent createSequenceFlowTakenEvent(Token token,
            WorkflowEventType type, String sequenceFlowId, FlowElement sourceActivity,
            Object sourceActivityBehavior, FlowElement targetActivity,
            Object targetActivityBehavior) {

        final SequenceFlowTakenWorkflowEvent event = new SequenceFlowTakenWorkflowEvent(type);

        final String sourceActivityBehaviorClass = sourceActivityBehavior != null
                ? sourceActivityBehavior.getClass().getCanonicalName() : null;
        final String targetActivityBehaviorClass = targetActivityBehavior != null
                ? targetActivityBehavior.getClass().getCanonicalName() : null;

        if (token != null) {
            event.setTokenId(token.getId());
            event.setProcessInstanceId(token.getProcessInstanceId());
            event.setProcessDefinitionId(token.getProcessDefinitionId());
        }
        event.setId(sequenceFlowId);
        event.setSourceActivityId(sourceActivity != null ? sourceActivity.getId() : null);
        event.setSourceActivityName(sourceActivity != null ? sourceActivity.getName() : null);
        event.setSourceActivityType(
                sourceActivity != null ? sourceActivity.getClass().getName() : null);
        event.setSourceActivityBehaviorClass(sourceActivityBehaviorClass);
        event.setTargetActivityId(sourceActivity != null ? targetActivity.getId() : null);
        event.setTargetActivityName(sourceActivity != null ? targetActivity.getName() : null);
        event.setTargetActivityType(
                sourceActivity != null ? targetActivity.getClass().getName() : null);
        event.setTargetActivityBehaviorClass(targetActivityBehaviorClass);

        return event;
    }

    public static ActivityWorkflowEvent createActivityEvent(WorkflowEventType type,
            String activityId, String activityName, Token token, FlowElement flowElement) {
        final ActivityWorkflowEvent event = new ActivityWorkflowEvent(type);
        event.setActivityId(activityId);
        event.setActivityName(activityName);
        event.setTokenId(token.getId());
        event.setProcessDefinitionId(token.getProcessDefinitionId());
        event.setProcessInstanceId(token.getProcessInstanceId());

        if (flowElement instanceof FlowNode) {
            final FlowNode flowNode = (FlowNode) flowElement;
            event.setActivityType(parseActivityType(flowNode));

            final Object behavior = flowNode.getBehavior();
            if (behavior != null) {
                event.setBehaviorClass(behavior.getClass().getCanonicalName());
            }
        }
        return event;
    }

    public static ActivityCancelledWorkflowEvent createActivityCancelledEvent(String activityId,
            String activityName, Long tokenId, Long processInstanceId, Long processDefinitionId,
            String activityType, String behaviorClassName, Object cause) {
        final ActivityCancelledWorkflowEvent event = new ActivityCancelledWorkflowEvent();
        event.setActivityId(activityId);
        event.setActivityName(activityName);
        event.setActivityType(activityType);
        event.setCause(cause);
        event.setProcessDefinitionId(processDefinitionId);
        event.setProcessInstanceId(processInstanceId);
        event.setTokenId(tokenId);
        return event;
    }

    public static MessageWorkflowEvent createMessageEvent(WorkflowEventType type, String activityId,
            String messageName, Object payload, Long tokenId, Long processInstanceId,
            Long processDefinitionId) {
        final MessageWorkflowEvent event = new MessageWorkflowEvent(type);
        event.setActivityId(activityId);
        event.setTokenId(tokenId);
        event.setProcessDefinitionId(processDefinitionId);
        event.setProcessInstanceId(processInstanceId);
        event.setMessageName(messageName);
        event.setPayload(payload);
        return event;
    }

    public static SignalWorkflowEvent createSignalEvent(WorkflowEventType type, String activityId,
            String signalName, Object signalData, Long tokenId, Long processInstanceId,
            Long processDefinitionId) {
        final SignalWorkflowEvent event = new SignalWorkflowEvent(type);
        event.setActivityId(activityId);
        event.setTokenId(tokenId);
        event.setProcessDefinitionId(processDefinitionId);
        event.setProcessInstanceId(processInstanceId);
        event.setSignalName(signalName);
        event.setSignalData(signalData);
        return event;
    }

    private static String parseActivityType(FlowNode flowNode) {
        String className = flowNode.getClass().getSimpleName();
        className = className.substring(0, 1).toLowerCase() + className.substring(1);
        return className;
    }

    private static void setEventContext(EntityWorkflowEvent event) {
        final Object entity = event.getEntity();

        if (entity instanceof JobEntity) {
            final JobEntity job = (JobEntity) entity;
            event.setTokenId(job.getTokenId());
            event.setProcessInstanceId(job.getProcessInstanceId());
            event.setProcessDefinitionId(job.getProcessDefinitionId());
        } else if (entity instanceof DelegateToken) {
            final DelegateToken token = (DelegateToken) entity;
            event.setTokenId(token.getId());
            event.setProcessInstanceId(token.getProcessInstanceId());
            event.setProcessDefinitionId(token.getProcessDefinitionId());
        } else if (entity instanceof ProcessDefinitionEntity) {
            final ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) entity;
            event.setProcessDefinitionId(processDefinition.getId());
        }
    }
}
