package com.kaaterskil.workflow.bpm.common.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class InputOutputSpecification extends BaseElement {

    @XmlElement(name = "inputSet", type = InputSet.class, required = true)
    private List<InputSet> inputSets = new ArrayList<>();

    @XmlElement(name = "outputSet", type = OutputSet.class, required = true)
    private List<OutputSet> outputSets = new ArrayList<>();

    @XmlElement(name = "dataInput", type = DataInput.class, required = false)
    private Set<DataInput> dataInputs = new TreeSet<>();

    @XmlElement(name = "dataOutput", type = DataOutput.class, required = false)
    private Set<DataOutput> dataOutputs = new TreeSet<>();

    /*---------- Methods ----------*/

    @Override
    public String toString() {
        return String.format(
                "InputOutpuSpecification [inputSets=%s, outputSets=%s, dataInputs=%s, dataOutputs=%s, id=%s, documentation=%s, extensionElements=%s]",
                inputSets, outputSets, dataInputs, dataOutputs, id, documentation, extensionElements);
    }

    /*---------- Getter/Setters ----------*/

    public List<InputSet> getInputSets() {
        return inputSets;
    }

    public void setInputSets(List<InputSet> inputSets) {
        this.inputSets = inputSets;
    }

    public List<OutputSet> getOutputSets() {
        return outputSets;
    }

    public void setOutputSets(List<OutputSet> outputSets) {
        this.outputSets = outputSets;
    }

    public Set<DataInput> getDataInputs() {
        return dataInputs;
    }

    public void setDataInputs(Set<DataInput> dataInputs) {
        this.dataInputs = dataInputs;
    }

    public Set<DataOutput> getDataOutputs() {
        return dataOutputs;
    }

    public void setDataOutputs(Set<DataOutput> dataOutputs) {
        this.dataOutputs = dataOutputs;
    }
}
