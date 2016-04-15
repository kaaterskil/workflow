package com.kaaterskil.workflow.bpm.common.gateway;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(String.class)
public enum GatewayDirection {

    UNSPECIFIED, CONVERGING, DIVERGING, MIXED;
}
