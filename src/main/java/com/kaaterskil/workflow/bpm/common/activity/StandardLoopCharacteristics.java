package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.Expression;

/**
 * The StandardLoopCharacteristics class defines looping behavior based on a boolean condition. The
 * Activity will loop as long as the boolean condition is true. The condition is evaluated for every
 * loop iteration, and MAY be evaluated at the beginning or at the end of the iteration. In
 * addition, a numeric cap can be optionally specified. The number of iterations MAY NOT exceed this
 * cap.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class StandardLoopCharacteristics extends LoopCharacteristics {

    /**
     * Flag that controls whether the loop condition is evaluated at the beginning (testBefore =
     * true) or at the end (testBefore = false) of the loop iteration.
     */
    @XmlAttribute
    private boolean testBefore = false;

    /**
     * Serves as a cap on the number of iterations.
     */
    @XmlAttribute
    private int loopMaximum = -1;

    /**
     * A boolean Expression that controls the loop. The Activity will only loop as long as this
     * condition is true. The looping behavior MAY be under-specified, meaning that the modeler can
     * simply document the condition, in which case the loop cannot be formally executed.
     */
    @XmlElement(name = "loopCondition", type = Expression.class, required = false)
    private Expression loopCondition;

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "StandardLoopCharacteristics [testBefore=%s, loopMaximum=%s, loopCondition=%s, loopCounter=%s, id=%s, documentation=%s, extensionElements=%s]",
                testBefore, loopMaximum, loopCondition, loopCounter, id, documentation,
                extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public boolean isTestBefore() {
        return testBefore;
    }

    public void setTestBefore(boolean testBefore) {
        this.testBefore = testBefore;
    }

    public int getLoopMaximum() {
        return loopMaximum;
    }

    public void setLoopMaximum(int loopMaximum) {
        this.loopMaximum = loopMaximum;
    }

    public Expression getLoopCondition() {
        return loopCondition;
    }

    public void setLoopCondition(Expression loopCondition) {
        this.loopCondition = loopCondition;
    }
}
