package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.SubProcessActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class SubProcessParseHandler extends AbstractActivityParseHandler<SubProcess> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return SubProcess.class;
    }

    @Override
    protected void executeParse(BpmParser parser, SubProcess element) {
        element.setBehavior(SubProcessActivityBehavior.class.getCanonicalName());

        parser.processFlowElements(element.getFlowElements());
    }

}
