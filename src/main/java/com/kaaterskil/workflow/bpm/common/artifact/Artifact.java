package com.kaaterskil.workflow.bpm.common.artifact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

/**
 * BPMN provides modelers with the capability of showing additional information about a Process that
 * is not directly related to the Sequence Flows or Message Flows of the Process.
 * <p>
 * At this point, BPMN provides three standard Artifacts: Associations, Groups, and Text
 * Annotations. Additional Artifacts MAY be added to the BPMN specification in later versions. A
 * modeler or modeling tool MAY extend a BPMN diagram and add new types of Artifacts to a Diagram.
 * Any new Artifact MUST follow the Sequence Flow and Message Flow connection rules (listed below).
 * Associations can be used to link Artifacts to Flow Objects (see page 67).
 * <ul>
 * <li>􏰀An Artifact MUST NOT be a target for a Sequence Flow. 􏰀
 * <li>An Artifact MUST NOT be a source for a Sequence Flow.
 * <li>An Artifact MUST NOT be a target for a Message Flow.
 * <li>An Artifact MUST NOT be a source for a Message Flow.
 * </ul>
 * </p>
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Artifact extends BaseElement {

}
