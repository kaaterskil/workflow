package com.kaaterskil.workflow.bpm.common.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.Expression;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class DataAssociation extends BaseElement {

    @XmlElement(name = "transformation", type = Expression.class, required = false)
    protected Expression transformation;

    @XmlElement(name = "assignment", type = Assignment.class, required = false)
    protected List<Assignment> assignments = new ArrayList<>();

    @XmlElementWrapper(name = "sourceRefs")
    @XmlElements({
            @XmlElement(name = "dataInput", type = DataInput.class),
            @XmlElement(name = "dataObject", type = DataObject.class),
            @XmlElement(name = "dataOutput", type = DataOutput.class),
            @XmlElement(name = "dataStore", type = DataStore.class), })
    protected List<BaseElement> sourceRefs = new ArrayList<>();

    @XmlElements({
            @XmlElement(name = "dataInput", type = DataInput.class),
            @XmlElement(name = "dataObject", type = DataObject.class),
            @XmlElement(name = "dataOutput", type = DataOutput.class),
            @XmlElement(name = "dataStore", type = DataStore.class), })
    protected BaseElement targetRef;

    /*---------- Getter/Setters ----------*/

    public Expression getTransformation() {
        return transformation;
    }

    public void setTransformation(Expression transformation) {
        this.transformation = transformation;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<BaseElement> getSourceRefs() {
        return sourceRefs;
    }

    public void setSourceRefs(List<BaseElement> sourceRefs) {
        this.sourceRefs = sourceRefs;
    }

    public BaseElement getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(BaseElement targetRef) {
        this.targetRef = targetRef;
    }
}
