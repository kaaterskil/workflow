package com.kaaterskil.workflow.bpm.common.artifact;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum(String.class)
public enum AssociationDirection {

    NONE, ONE, BOTH;
}
