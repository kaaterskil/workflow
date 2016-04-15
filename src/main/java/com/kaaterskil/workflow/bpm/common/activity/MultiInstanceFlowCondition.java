package com.kaaterskil.workflow.bpm.common.activity;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(String.class)
public enum MultiInstanceFlowCondition {

    NONE, ONE, ALL, COMPLEX;
}
