package com.kaaterskil.workflow.bpm.foundation;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * RootElement is the abstract super class for all BPMN elements that are contained within
 * Definitions. When contained within Definitions, these elements have their own defined life-cycle
 * and are not deleted with the deletion of other elements. Examples of concrete RootElements
 * include Collaboration, Process, and Choreography. Depending on their use, RootElements can be
 * referenced by multiple other elements (i.e., they can be reused). Some RootElements MAY be
 * contained within other elements instead of Definitions. This is done to avoid the maintenance
 * overhead of an independent life-cycle. For example, an EventDefinition would be contained in a
 * Process since it is used only there. In this case the EventDefinition would be dependent on the
 * tool life-cycle of the Process.
 *
 * @author bcaple
 */
@XmlRootElement
public abstract class RootElement extends BaseElement {

}
