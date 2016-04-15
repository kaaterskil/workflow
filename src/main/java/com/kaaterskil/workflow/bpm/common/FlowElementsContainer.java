package com.kaaterskil.workflow.bpm.common;

import java.util.List;

import com.kaaterskil.workflow.bpm.common.artifact.Artifact;

/**
 * FlowElementsContainer is an abstract super class for BPMN diagrams (or views) and defines the
 * superset of elements that are contained in those diagrams. Basically, a FlowElementsContainer
 * contains FlowElements, which are Events (see page 233), Gateways (see page 287), Sequence Flows
 * (see page 97), Activities (see page 151), and Choreography Activities (see page 321). There are
 * four (4) types of FlowElementsContainers (see Figure 8.23): Process, Sub-Process, Choreography,
 * and Sub-Choreography.
 *
 * @author bcaple
 */
public interface FlowElementsContainer {

    FlowElement getFlowElement(String flowElementId, boolean searchRecursively);

    void addFlowElement(FlowElement flowElement);

    void removeFlowElement(String id);

    List<FlowElement> getFlowElements();

    Artifact getArtifact(String id);

    void addArtifact(Artifact artifact);

    void removeArtifact(String id);

    List<Artifact> getArtifacts();
}
