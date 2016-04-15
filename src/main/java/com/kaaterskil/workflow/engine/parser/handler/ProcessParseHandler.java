package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class ProcessParseHandler extends AbstractParseHandler<Process> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return Process.class;
    }

    @Override
    protected void executeParse(BpmParser parser, Process process) {
        parser.processFlowElements(process.getFlowElements());
    }

}
