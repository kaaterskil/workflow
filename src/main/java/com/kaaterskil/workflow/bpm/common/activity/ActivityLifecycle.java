package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(String.class)
public enum ActivityLifecycle {

    /**
     * An Activity is Ready for execution if the REQUIRED number of tokens is available to activate
     * the Activity. The REQUIRED number of tokens (one or more) is indicated by the attribute
     * StartQuantity. If the Activity has more than one Incoming Sequence Flows, there is an implied
     * Exclusive Gateway that defines the behavior.
     */
    READY,

    /**
     * When some data InputSet becomes available, the Activity changes from Ready to the Active
     * state. The availability of a data InputSet is evaluated as follows. The data InputSets are
     * evaluated in order. For each InputSet, the data inputs are filled with data coming from the
     * elements of the context such as Data Objects or Properties by triggering the input Data
     * Associations. An InputSet is available if each of its REQUIRED Data Inputs is available. A
     * data input is REQUIRED by a data InputSet if it is not optional in that InputSet. If an
     * InputSet is available, it is used to start the Activity. Further InputSets are not evaluated.
     * If an InputSet is not available, the next InputSet is evaluated. The Activity waits until one
     * InputSet becomes available. Please refer to Section 10.3.2 (page 225) for a description of
     * the execution semantics for Data Associations.
     */
    ACTIVE,

    /**
     * An Activity, if Ready or Active, can be Withdrawn from being able to complete in the context
     * of a race condition. This situation occurs for Tasks that are attached after an Event-Based
     * Exclusive Gateway. The first element (Task or Event) that completes causes all other Tasks to
     * be withdrawn.
     */
    WITHDRAWN,

    /**
     * If an Activity fails during execution, it changes from the state Active to Failed. If a fault
     * happens in the environment of the Activity, termination of the Activity is triggered, causing
     * the Activity to go into the state Terminated.
     */
    FAILD, TERMINATED,

    /**
     * If an Activity’s execution ends without anomalies, the Activity’s state changes to
     * Completing. This intermediate state caters for processing steps prior to completion of the
     * Activity. An example of where this is useful is when non-interrupting Event Handlers
     * (proposed for BPMN 2.0) are attached to an Activity. They need to complete before the
     * Activity to which it is attached can complete. The state Completing of the main Activity
     * indicates that the execution of the main Activity has been completed, however, the main
     * Activity is not allowed to be in the state Completed, as it still has to wait for all
     * non-interrupting Event Handlers to complete. The state Completing does not allow further
     * processing steps, otherwise allowed during the execution of the Activity. For example, new
     * attached non-interrupting Event Handlers MAY be created as long as the main Activity is in
     * state Active. However, once in the state Completing, running handlers should be completed
     * with no possibility to create new ones.
     */
    COMPLETING,

    /**
     * An Activity’s execution is interrupted if an interrupting Event is raised (such as an error)
     * or if an interrupting Event Sub-Process is initiated, In this case, the Activity’s state
     * changes to Failing (in case of an error) or Terminating (in case any other interrupting
     * Event). All nested Activities that are not in Ready, Active or a final state (Completed,
     * Compensated, Failed, etc.) and non-interrupting Event Sub-Processes are terminated. The data
     * context of the Activity is preserved in case an interrupting Event Sub-Process is invoked.
     * The data context is released after the Event Sub-Process reaches a final state.
     */
    FAILING, TERMINATING,

    /**
     * After all completion dependencies have been fulfilled, the state of the Activity changes to
     * Completed. The outgoing Sequence Flows becomes active and a number of tokens, indicated by
     * the attribute CompletionQuantity, is placed on it. If there is more than one outbound
     * Sequence Flows for an Activity, it behaves like an implicit Parallel Gateway. Upon
     * completion, also a data OutputSet of the Activity is selected as follows. All OutputSets are
     * checked for availability in order. An OutputSet is available if all its REQUIRED Data Outputs
     * are available. A data output is REQUIRED by an OutputSet if it is not optional in that
     * OutputSet. If the data OutputSet is available, data is pushed into the context of the
     * Activity by triggering the output Data Associations of all its data outputs. Further
     * OutputSets are not evaluated. If the data OutputSet is not available, the next data OutputSet
     * is checked. If no OutputSet is available, a runtime exception is thrown. If the Activity has
     * an associated IORule, the chosen OutputSet is checked against that IORule, i.e., it is
     * checked whether the InputSet that was used in starting the Activity instance is together with
     * the chosen OutputSet compliant with the IORule. If not, a runtime exception is thrown.
     */
    COMPLETED,

    /**
     * Only completed Activities could, in principle, be compensated, however, the Activity can end
     * in state Completed, as compensation might not be triggered or there might be no compensation
     * handler specified. If the compensation handler is invoked, the Activity changes to state
     * Compensating until either compensation finishes successfully (state Compensated), an
     * exception occurs (state Failed), or controlled or uncontrolled termination is triggered
     * (state Terminated).
     */
    COMPENSATING, COMPENSATED, FAILED;
}
