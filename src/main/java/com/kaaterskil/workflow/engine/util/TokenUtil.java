package com.kaaterskil.workflow.engine.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowElementsContainer;
import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.util.CollectionUtil;

public class TokenUtil {

    public static boolean isReachable(Long processDefinitionId, String sourceElementId,
            String targetElementId) {
        final Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);

        final FlowElement sourceFlowElement = process.getFlowElement(sourceElementId, true);
        FlowNode sourceElement = null;
        if (sourceFlowElement instanceof FlowNode) {
            sourceElement = (FlowNode) sourceFlowElement;
        } else if (sourceFlowElement instanceof SequenceFlow) {
            sourceElement = (FlowNode) ((SequenceFlow) sourceFlowElement).getTargetFlowElement();
        }
        if (sourceElement == null) {
            throw new WorkflowException("No source element found for id " + sourceElementId
                    + " in process definition " + processDefinitionId);
        }

        final FlowElement targetFlowElement = process.getFlowElement(targetElementId, true);
        FlowNode targetElement = null;
        if (targetFlowElement instanceof FlowNode) {
            targetElement = (FlowNode) targetFlowElement;
        } else if (targetFlowElement instanceof SequenceFlow) {
            targetElement = (FlowNode) ((SequenceFlow) targetFlowElement).getTargetFlowElement();
        }
        if (targetElement == null) {
            throw new WorkflowException("No target element found for id " + targetElementId
                    + " in process definition " + processDefinitionId);
        }

        final Set<String> visitedElementRefs = new HashSet<>();
        return isReachable(process, sourceElement, targetElement, visitedElementRefs);
    }

    public static boolean isReachable(Process process, FlowNode sourceElement,
            FlowNode targetElement, Set<String> visitedElementRefs) {

        if (sourceElement.getOutgoing().size() == 0) {
            visitedElementRefs.add(sourceElement.getId());

            final FlowElementsContainer parent = process.findParent(sourceElement);
            if (parent != null && parent instanceof SubProcess) {
                sourceElement = (SubProcess) parent;
            } else {
                return false;
            }
        }

        if (sourceElement.getId().equals(targetElement.getId())) {
            return true;
        }

        visitedElementRefs.add(sourceElement.getId());

        final List<SequenceFlow> sequenceFlows = sourceElement.getOutgoing();
        if (CollectionUtil.isNotEmpty(sequenceFlows)) {
            for (final SequenceFlow sequenceFlow : sequenceFlows) {
                final String targetRef = sequenceFlow.getTargetRef();
                final FlowNode target = (FlowNode) process.getFlowElement(targetRef, true);
                if (target != null && !visitedElementRefs.contains(targetRef)) {
                    final boolean reachable = isReachable(process, target, targetElement,
                            visitedElementRefs);
                    if (reachable) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
