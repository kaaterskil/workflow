package com.kaaterskil.workflow.bpm.common.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataInputAssociation extends DataAssociation {

    @Override
    public String toString() {
        return String.format(
                "DataInputAssociation [transformation=%s, assignments=%s, sourceRefs=%s, targetRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                transformation, assignments, sourceRefs, targetRef, id, documentation,
                extensionElements);
    }

}
