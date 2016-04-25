package com.kaaterskil.workflow.engine.parser.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class SequenceFlowParseHandler extends AbstractParseHandler<SequenceFlow> {
    private static final Logger log = LoggerFactory.getLogger(SequenceFlowParseHandler.class);

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return SequenceFlow.class;
    }

    @Override
    protected void executeParse(BpmParser parser, SequenceFlow sequenceFlow) {
        log.debug("Parsing sequence flow " + sequenceFlow.getId());
        final Process process = parser.getCurrentProcess();

        sequenceFlow
                .setSourceFlowElement(process.getFlowElement(sequenceFlow.getSourceRef(), true));
        sequenceFlow
                .setTargetFlowElement(process.getFlowElement(sequenceFlow.getTargetRef(), true));
    }

}
