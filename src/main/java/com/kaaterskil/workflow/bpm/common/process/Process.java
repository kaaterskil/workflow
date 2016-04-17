package com.kaaterskil.workflow.bpm.common.process;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.HasTokenListeners;
import com.kaaterskil.workflow.bpm.Listener;
import com.kaaterskil.workflow.bpm.common.CallableElement;
import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowElementsContainer;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.activity.AdHocSubProcess;
import com.kaaterskil.workflow.bpm.common.activity.BusinessRuleTask;
import com.kaaterskil.workflow.bpm.common.activity.CallActivity;
import com.kaaterskil.workflow.bpm.common.activity.EventSubProcess;
import com.kaaterskil.workflow.bpm.common.activity.NoneTask;
import com.kaaterskil.workflow.bpm.common.activity.ReceiveTask;
import com.kaaterskil.workflow.bpm.common.activity.ScriptTask;
import com.kaaterskil.workflow.bpm.common.activity.SendTask;
import com.kaaterskil.workflow.bpm.common.activity.ServiceTask;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.activity.Task;
import com.kaaterskil.workflow.bpm.common.activity.Transaction;
import com.kaaterskil.workflow.bpm.common.artifact.Artifact;
import com.kaaterskil.workflow.bpm.common.artifact.CategoryValue;
import com.kaaterskil.workflow.bpm.common.artifact.Group;
import com.kaaterskil.workflow.bpm.common.artifact.TextAnnotation;
import com.kaaterskil.workflow.bpm.common.data.DataObject;
import com.kaaterskil.workflow.bpm.common.data.DataStore;
import com.kaaterskil.workflow.bpm.common.data.Property;
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Process extends CallableElement implements FlowElementsContainer, HasTokenListeners {

    @XmlAttribute
    private ProcessType processType = ProcessType.NONE;

    @XmlAttribute
    private boolean isExecutable;

    @XmlAttribute
    private boolean isClosed = false;

    @XmlAttribute
    private String state;

    @XmlElement(name = "property", type = Property.class, required = false)
    private List<Property> properties = new ArrayList<>();

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
    private List<FlowElement> flowElements = new ArrayList<>();

    @XmlElementWrapper(name = "artifacts")
    @XmlElements({
            @XmlElement(name = "textAnnotation", type = TextAnnotation.class),
            @XmlElement(name = "group", type = Group.class),
            @XmlElement(name = "category", type = CategoryValue.class) })
    private List<Artifact> artifacts = new ArrayList<>();

    @XmlElement(name = "tokenListener", type = Listener.class, required = false)
    protected List<Listener> tokenListeners = new ArrayList<>();

    /*---------- Instance properties ----------*/

    @XmlTransient
    private FlowElement initialFlowElement;

    public FlowElement getInitialFlowElement() {
        return initialFlowElement;
    }

    public void setInitialFlowElement(FlowElement initialFlowElement) {
        this.initialFlowElement = initialFlowElement;
    }

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

    @SuppressWarnings("unchecked")
    public <E extends FlowElement> List<E> findFlowElements(Class<E> type) {
        final List<E> foundFlowElements = new ArrayList<>();
        for (final FlowElement each : this.getFlowElements()) {
            if (type.isInstance(each)) {
                foundFlowElements.add((E) each);
            }
        }
        return foundFlowElements;
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

    @Override
    public String toString() {
        return String.format(
                "Process [processType=%s, isExecutable=%s, artifacts=%s, isClosed=%s, properties=%s, state=%s, flowElements=%s, name=%s, ioSpecification=%s, ioBinding=%s, id=%s, documentation=%s, extensionElements=%s]",
                processType, isExecutable, artifacts, isClosed, properties, state, flowElements,
                name, ioSpecification, ioBinding, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public boolean getIsExecutable() {
        return isExecutable;
    }

    public void setExecutable(boolean isExecutable) {
        this.isExecutable = isExecutable;
    }

    @Override
    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(ArrayList<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public List<FlowElement> getFlowElements() {
        return flowElements;
    }

    public void setFlowElements(ArrayList<FlowElement> flowElements) {
        this.flowElements = flowElements;
    }

    @Override
    public List<Listener> getTokenListeners() {
        return tokenListeners;
    }

    @Override
    public void setTokenListeners(List<Listener> tokenListeners) {
        this.tokenListeners = tokenListeners;
    }
}
