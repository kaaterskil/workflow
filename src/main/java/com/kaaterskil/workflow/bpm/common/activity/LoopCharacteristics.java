package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

/**
 * Activities MAY be repeated sequentially, essentially behaving like a loop. The presence of
 * LoopCharacteristics signifies that the Activity has looping behavior. LoopCharacteristics is an
 * abstract class. Concrete subclasses define specific kinds of looping behavior.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class LoopCharacteristics extends BaseElement {

    /**
     * The LoopCounter attribute is used at runtime to count the number of loops and is
     * automatically updated by the process engine.
     */
    @XmlTransient
    protected int loopCounter;

    /*---------- Getter/Setters ----------*/

    public int getLoopCounter() {
        return loopCounter;
    }

    public void setLoopCounter(int loopCounter) {
        this.loopCounter = loopCounter;
    }
}
