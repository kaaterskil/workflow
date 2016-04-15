package com.kaaterskil.workflow.engine.service;

import com.kaaterskil.workflow.engine.persistence.entity.ActivityEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public interface ActivityService {

    void recordActivityEnd(Token token);

    ActivityEntity findActivityEntity(Token token, boolean createIfNotFound);

    ActivityEntity create();

}
