package com.kaaterskil.workflow.engine.delegate.event;

import java.util.ArrayList;
import java.util.List;

import com.kaaterskil.workflow.util.ValidationUtils;

public enum WorkflowEventType {

    /**
     * A new entity is created. The new entity is contained in the event.
     */
    ENTITY_CREATED,

    /**
     * A new entity has been created and is fully initialized. If any children are created as part
     * of the creation of an entity, this event will be fired AFTER the create/initialization of the
     * child entities as opposed to the ENTITY_CREATE event.
     */
    ENTITY_INITIALIZED,

    /**
     * An existing entity is updated. The updated entity is contained in the event.
     */
    ENTITY_UPDATED,

    /**
     * An existing entity is deleted. The deleted entity is contained in the event.
     */
    ENTITY_DELETED,

    /**
     * An existing entity is activated. The activated entity is contained in the event. Will be
     * dispatched for ProcessDefinitions, ProcessInstances and Tasks.
     */
    ENTITY_ACTIVATED,

    /**
     * A job has been executed successfully. The event contains the job that was executed.
     */
    JOB_EXECUTION_SUCCESS,

    /**
     * The execution of a job has failed. The event contains the job that was executed and the
     * exception.
     */
    JOB_EXECUTION_FAILURE,

    /**
     * The number of job retries have been decremented due to a failed job. The event contains the
     * job that was updated.
     */
    JOB_RETRIES_DECREMENTED,

    /**
     * A job has been canceled. The event contains the job that was canceled. Job can be canceled by
     * API call, task was completed and associated boundary timer was canceled, on the new process
     * definition deployment.
     */
    JOB_CANCELLED,

    /**
     * An activity is starting to execute
     */
    ACTIVITY_STARTED,

    /**
     * An activity has completed successfully
     */
    ACTIVITY_COMPLETED,

    /**
     * An activity is going to be cancelled. There can be three reasons for activity cancellation
     * (MessageEventSubscriptionEntity, SignalEventSubscriptionEntity, TimerEntity).
     */
    ACTIVITY_CANCELLED,

    /**
     * An activity has received a signal. Dispatched after the activity has responded to the signal.
     */
    ACTIVITY_SIGNALED,

    /**
     * An activity is about to be executed as a compensation for another activity. The event targets
     * the activity that is about to be executed for compensation.
     */
    ACTIVITY_COMPENSATE,

    /**
     * An activity has received an error event. Dispatched before the actual error has been handled
     * by the activity. The event’s activityId contains a reference to the error-handling activity.
     * This event will be either followed by a ACTIVITY_SIGNALLED event or ACTIVITY_COMPLETE for the
     * involved activity, if the error was delivered successfully.
     */
    ACTIVITY_ERROR_RECEIVED,

    /**
     * An activity has received a message event. Dispatched before the actual message has been
     * received by the activity.
     */
    ACTIVITY_MESSAGE_RECEIVED,
    HISTORIC_ACTIVITY_INSTANCE_CREATED,
    HISTORIC_ACTIVITY_INSTANCE_ENDED, SEQUENCE_FLOW_TAKEN, PROCESS_STARTED,

    /**
     * A process has been completed. Dispatched after the last activity ACTIVITY_COMPLETED event.
     * Process is completed when it reaches state in which process instance does not have any
     * transition to take.
     */
    PROCESS_COMPLETED, PROCESS_COMPLETED_WITH_ERROR,

    /**
     * A process has been cancelled. Dispatched before the process instance is deleted from runtime.
     * Process instance is cancelled by API call RuntimeService.deleteProcessInstance
     */
    PROCESS_CANCELLED, HISTORIC_PROCESS_INSTANCE_CREATED, HISTORIC_PROCESS_INSTANCE_ENDED;

    /**
     * Parses a comma-delimited string into a list of event types.
     *
     * @param eventTypes
     * @return
     */
    public static WorkflowEventType[] parseTypes(String eventTypes) {
        final WorkflowEventType[] emptyArray = new WorkflowEventType[] {};
        final List<WorkflowEventType> result = new ArrayList<>();

        if (!ValidationUtils.isEmptyOrWhitespace(eventTypes)) {
            final String[] matches = eventTypes.split(",");
            final int len = matches.length;
            for (int i = 0; i < len; i++) {
                final WorkflowEventType type = WorkflowEventType.valueOf(matches[i].trim());
                if (!result.contains(type)) {
                    result.add(type);
                }
            }
        }
        return result.toArray(emptyArray);
    }
}
