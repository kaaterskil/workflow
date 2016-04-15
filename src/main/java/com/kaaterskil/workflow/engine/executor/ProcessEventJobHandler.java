package com.kaaterskil.workflow.engine.executor;

import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.EventSubscriptionService;

public class ProcessEventJobHandler implements JobHandler {

    @Override
    public String getType() {
        return JobHandler.EVENT_TYPE;
    }

    @Override
    public void execute(JobEntity job, Long config, Token execution,
            CommandContext commandContext) {

        final EventSubscriptionService service = commandContext.getEventSubscriptionService();
        final EventSubscription eventSubscription = service.findById(config);
        if (eventSubscription != null) {
            service.eventReceived(eventSubscription, null, false);
        }
    }

}
