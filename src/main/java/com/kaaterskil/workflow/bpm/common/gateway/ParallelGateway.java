package com.kaaterskil.workflow.bpm.common.gateway;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Parallel Gateway is used to synchronize (combine) parallel flows and to create parallel flows.
 *
 * @author bcaple
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ParallelGateway extends Gateway {

    @Override
    public String toString() {
        return String.format(
                "ParallelGateway [gatewayDirection=%s, incoming=%s, outgoing=%s, name=%s, categoryValueRef=%s, id=%s, documentation=%s, extensionElements=%s]",
                gatewayDirection, incoming, outgoing, name, categoryValueRef, id, documentation,
                extensionElements);
    }

}
