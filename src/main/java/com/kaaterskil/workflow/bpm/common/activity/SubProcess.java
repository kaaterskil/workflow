package com.kaaterskil.workflow.bpm.common.activity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowElementsContainer;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.artifact.Artifact;
import com.kaaterskil.workflow.bpm.common.artifact.CategoryValue;
import com.kaaterskil.workflow.bpm.common.artifact.Group;
import com.kaaterskil.workflow.bpm.common.artifact.TextAnnotation;
import com.kaaterskil.workflow.bpm.common.data.DataObject;
import com.kaaterskil.workflow.bpm.common.data.DataStore;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.event.EndEvent;
import com.kaaterskil.workflow.bpm.common.event.ImplicitThrowEvent;
import com.kaaterskil.workflow.bpm.common.event.IntermediateCatchEvent;
import com.kaaterskil.workflow.bpm.common.event.IntermediateThrowEvent;
import com.kaaterskil.workflow.bpm.common.event.StartEvent;
import com.kaaterskil.workflow.bpm.common.gateway.ComplexGateway;
import com.kaaterskil.workflow.bpm.common.gateway.EventBasedGateway;
import com.kaaterskil.workflow.bpm.common.gateway.ExclusiveGateway;
import com.kaaterskil.workflow.bpm.common.gateway.InclusiveGateway;
import com.kaaterskil.workflow.bpm.common.gateway.ParallelGateway;

/**
 * A Sub-Process is an Activity whose internal details have been modeled using Activities, Gateways,
 * Events, and Sequence Flows. A Sub-Process is a graphical object within a Process, but it also can
 * be “opened up” to show a lower-level Process. Sub-Processes define a contextual scope that can be
 * used for attribute visibility, transactional scope, for the handling of exceptions (see page 275
 * for more details), of Events, or for compensation (see page 302 for more details).
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SubProcess extends Activity implements FlowElementsContainer {

    @XmlElementWrapper(name = "flowElements")
    @XmlElements({
            @XmlElement(name = "dataObject", type = DataObject.class),
            @XmlElement(name = "dataStore", type = DataStore.class),
            @XmlElement(name = "callActivity", type = CallActivity.class),
            @XmlElement(name = "subProcess", type = SubProcess.class),
            @XmlElement(name = "adHocSubProcess", type = AdHocSubProcess.class),
            @XmlElement(name = "eventSubProcess", type = EventSubProcess.class),
            @XmlElement(name = "transaction", type = Transaction.class),
            @XmlElement(name = "task", type = Task.class),
            @XmlElement(name = "businessRuleTask", type = BusinessRuleTask.class),
            @XmlElement(name = "noneTask", type = NoneTask.class),
            @XmlElement(name = "receiveTask", type = ReceiveTask.class),
            @XmlElement(name = "scriptTask", type = ScriptTask.class),
            @XmlElement(name = "sendTask", type = SendTask.class),
            @XmlElement(name = "serviceTask", type = ServiceTask.class),
            @XmlElement(name = "boundaryEvent", type = BoundaryEvent.class),
            @XmlElement(name = "intermediateCatchEvent", type = IntermediateCatchEvent.class),
            @XmlElement(name = "startEvent", type = StartEvent.class),
            @XmlElement(name = "endEvent", type = EndEvent.class),
            @XmlElement(name = "implicitThrowEvent", type = ImplicitThrowEvent.class),
            @XmlElement(name = "intermediateThrowEvent", type = IntermediateThrowEvent.class),
            @XmlElement(name = "complexGateway", type = ComplexGateway.class),
            @XmlElement(name = "eventBasedGateway", type = EventBasedGateway.class),
            @XmlElement(name = "exclusiveGateway", type = ExclusiveGateway.class),
            @XmlElement(name = "inclusiveGateway", type = InclusiveGateway.class),
            @XmlElement(name = "parallelGateway", type = ParallelGateway.class),
            @XmlElement(name = "sequenceFlow", type = SequenceFlow.class) })
    protected List<FlowElement> flowElements = new ArrayList<>();

    /**
     * This attribute provides the list of Artifacts that are contained within the Sub-Process.
     */
    @XmlElementWrapper(name = "artifacts")
    @XmlElements({
            @XmlElement(name = "textAnnotation", type = TextAnnotation.class),
            @XmlElement(name = "group", type = Group.class),
            @XmlElement(name = "category", type = CategoryValue.class) })
    protected List<Artifact> artifacts = new ArrayList<>();

    /*---------- Methods ----------*/

    @Override
    public FlowElement getFlowElement(String flowElementId, boolean searchRecursively) {
        if(searchRecursively) {
            return getFlowElementRecursively(this, flowElementId);
        }
        return getFlowElementNonRecursively(flowElementId);
    }

    private FlowElement getFlowElementNonRecursively(String flowElementId) {
        if (flowElements != null && !flowElements.isEmpty()) {
            for (final FlowElement flowElement : flowElements) {
                if (flowElement.getId().equals(flowElementId)) {
                    return flowElement;
                }
            }
        }
        return null;
    }

    private FlowElement getFlowElementRecursively(FlowElementsContainer container,
            String flowElementId) {
        for (final FlowElement flowElement : container.getFlowElements()) {
            if (flowElement.getId() != null && flowElement.getId().equals(flowElementId)) {
                return flowElement;
            } else if (flowElement instanceof FlowElementsContainer) {
                final FlowElement childFlowElement = getFlowElementRecursively(
                        (FlowElementsContainer) flowElement, flowElementId);
                if (childFlowElement != null) {
                    return childFlowElement;
                }
            }
        }
        return null;
    }

    @Override
    public void addFlowElement(FlowElement flowElement) {
        flowElements.add(flowElement);
    }

    @Override
    public void removeFlowElement(String id) {
        final FlowElement flowElement = getFlowElementNonRecursively(id);
        if (flowElement != null) {
            flowElements.remove(flowElement);
        }
    }

    @Override
    public Artifact getArtifact(String id) {
        if (artifacts != null && !artifacts.isEmpty()) {
            for (final Artifact each : artifacts) {
                if (each.getId().equals(id)) {
                    return each;
                }
            }
        }
        return null;
    }

    @Override
    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
    }

    @Override
    public void removeArtifact(String id) {
        final Artifact artifact = getArtifact(id);
        if (artifact != null) {
            artifacts.remove(artifact);
        }
    }

    /*---------- Getter/Setters ----------*/

    public boolean isTriggeredByEvent() {
        return false;
    }

    @Override
    public List<FlowElement> getFlowElements() {
        return flowElements;
    }

    public void setFlowElements(List<FlowElement> flowElements) {
        this.flowElements = flowElements;
    }

    @Override
    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }
}
