package com.kaaterskil.workflow.bpm.common.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class BoundaryEvent extends CatchEvent {

    @XmlAttribute
    private String attachedToRef;

    @XmlAttribute
    private boolean cancelActivity = true;

    /**
     * The FQCN of the behavior bean.
     */
    @XmlAttribute
    private String behavior;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "BoundaryEvent [attachedToRef=%s, cancelActivity=%s, behavior=%s, eventDefinitionRefs=%s, eventDefinitions=%s, dataOutputAssociations=%s, dataOutputs=%s, outputSet=%s, parallelMultiple=%s, properties=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, tokenListeners=%s, id=%s, documentation=%s, extensionElements=%s]",
                attachedToRef, cancelActivity, behavior, eventDefinitionRefs, eventDefinitions,
                dataOutputAssociations, dataOutputs, outputSet, parallelMultiple, properties,
                incoming, outgoing, name, categoryValueRef, tokenListeners, id, documentation,
                extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getAttachedToRef() {
        return attachedToRef;
    }

    public void setAttachedToRef(String attachedToRef) {
        this.attachedToRef = attachedToRef;
    }

    public boolean isCancelActivity() {
        return cancelActivity;
    }

    public void setCancelActivity(boolean cancelActivity) {
        this.cancelActivity = cancelActivity;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}
