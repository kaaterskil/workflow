package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class SequenceFlowParseHandler extends AbstractParseHandler<SequenceFlow> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return SequenceFlow.class;
    }

    @Override
    protected void executeParse(BpmParser parser, SequenceFlow sequenceFlow) {
        final Process process = parser.getProcess();

        sequenceFlow
                .setSourceFlowElement(process.getFlowElement(sequenceFlow.getSourceRef(), true));
        sequenceFlow
                .setTargetFlowElement(process.getFlowElement(sequenceFlow.getTargetRef(), true));
    }

}
