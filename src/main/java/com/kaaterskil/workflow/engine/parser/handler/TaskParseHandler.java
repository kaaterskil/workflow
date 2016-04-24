package com.kaaterskil.workflow.engine.parser.handler;

import com.kaaterskil.workflow.bpm.common.activity.Task;
import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.behavior.TaskActivityBehavior;
import com.kaaterskil.workflow.engine.parser.BpmParser;

public class TaskParseHandler extends AbstractActivityParseHandler<Task> {

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return Task.class;
    }

    @Override
    protected void executeParse(BpmParser parser, Task element) {
        element.setBehavior(TaskActivityBehavior.class.getCanonicalName());
    }

}
