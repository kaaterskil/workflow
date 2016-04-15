package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Script Task is executed by a business process engine. The modeler or implementer defines a
 * script in a language that the engine can interpret. When the Task is ready to start, the engine
 * will execute the script. When the script is completed, the Task will also be completed.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ScriptTask extends Task {

    /**
     * Defines the format of the script. This attribute value MUST be specified with a mime-type
     * format. And it MUST be specified if a script is provided.
     */
    @XmlAttribute
    private String scriptFormat;

    /**
     * The modeler MAY include a script that can be run when the Task is performed. If a script is
     * not included, then the Task will act as the equivalent of an Abstract Task.
     */
    @XmlAttribute
    private String script;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "ScriptTask [scriptFormat=%s, script=%s, isForCompensation=%s, loopCharacteristics=%s, defaultSequenceFlow=%s, ioSpecification=%s, properties=%s, dataInputAssociations=%s, dataOutputAssociations=%s, startQuantity=%s, completionQuantity=%s, state=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                scriptFormat, script, isForCompensation, loopCharacteristics, defaultSequenceFlow,
                ioSpecification, properties, dataInputAssociations, dataOutputAssociations,
                startQuantity, completionQuantity, state, incoming, outgoing, name,
                categoryValueRef, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public String getScriptFormat() {
        return scriptFormat;
    }

    public void setScriptFormat(String scriptFormat) {
        this.scriptFormat = scriptFormat;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
