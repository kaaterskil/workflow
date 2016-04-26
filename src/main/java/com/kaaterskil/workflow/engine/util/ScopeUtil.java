package com.kaaterskil.workflow.engine.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.persistence.entity.CompensateEventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.EventSubscriptionService;
import com.kaaterskil.workflow.engine.service.TokenService;

public class ScopeUtil {

    public static void throwCompensationEvent(
            List<CompensateEventSubscription> eventSubscriptions, DelegateToken token,
            boolean isAsync) {

        final TokenService tokenService = Context.getCommandContext().getTokenService();
        final EventSubscriptionService eventSubscriptionService = Context.getCommandContext()
                .getEventSubscriptionService();

        // Test if the compensating token is already created, or create a new one. In either case,
        // make it a child of the executing token.
        for (final CompensateEventSubscription subscription : eventSubscriptions) {
            Token compensationToken = null;
            if (subscription.getConfiguration() != null) {
                final Long tokenId = Long.valueOf(subscription.getConfiguration());
                compensationToken = tokenService.findById(tokenId);
                compensationToken.setParent((Token) token);
                compensationToken.setEventScope(false);
            } else {
                compensationToken = tokenService.createChildToken((Token) token);
                subscription.setConfiguration(compensationToken.getId().toString());
            }
            // TODO set concurrent?
        }

        // Sort in reverse timestamp order
        Collections.sort(eventSubscriptions, new Comparator<EventSubscription>() {
            @Override
            public int compare(EventSubscription o1, EventSubscription o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });

        for (final CompensateEventSubscription each : eventSubscriptions) {
            eventSubscriptionService.eventReceived(each, null, isAsync);
        }
    }

    public static void createCopyOfSubProcessTokenForCompensation(Token subProcessToken,
            Token parentToken) {
        final EventSubscriptionService eventSubscriptionService = Context.getCommandContext()
                .getEventSubscriptionService();
        final List<EventSubscription> eventSubscriptions = eventSubscriptionService
                .findByTokenAndEventType(subProcessToken, EventSubscriptionType.COMPENSATE);

        final List<CompensateEventSubscription> subscriptions = new ArrayList<>();
        for (final EventSubscription each : eventSubscriptions) {
            if (each instanceof CompensateEventSubscription) {
                subscriptions.add((CompensateEventSubscription) each);
            }
        }

        if (!subscriptions.isEmpty()) {
            final Token eventScopeToken = Context.getCommandContext().getTokenService()
                    .createChildToken(parentToken);
            eventScopeToken.setActive(false);
            eventScopeToken.setEventScope(true);
            eventScopeToken.setCurrentFlowElement(subProcessToken.getCurrentFlowElement());

            final Map<String, Object> variables = subProcessToken.getVariables();
            for (final Entry<String, Object> variable : variables.entrySet()) {
                eventScopeToken.setVariable(variable.getKey(), variable.getValue());
            }

            for (final CompensateEventSubscription each : subscriptions) {
                eventSubscriptionService.delete(each);

                final CompensateEventSubscription newSubscription = eventSubscriptionService
                        .insertCompensationEvent(eventScopeToken, each.getActivityId());
                newSubscription.setConfiguration(each.getConfiguration());
                newSubscription.setCreatedAt(each.getCreatedAt());
                eventSubscriptionService.save(newSubscription);
            }

            final CompensateEventSubscription subscription = eventSubscriptionService
                    .insertCompensationEvent(parentToken,
                            eventScopeToken.getCurrentFlowElement().getId());
            subscription.setConfiguration(eventScopeToken.getId().toString());
        }
    }
}
