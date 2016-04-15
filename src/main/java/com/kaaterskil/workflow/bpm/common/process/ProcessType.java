package com.kaaterskil.workflow.bpm.common.process;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(value = String.class)
public enum ProcessType {

    NONE, PRIVATE, PUBLIC;
}
