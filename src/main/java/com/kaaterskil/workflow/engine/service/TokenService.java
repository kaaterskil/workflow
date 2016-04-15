package com.kaaterskil.workflow.engine.service;

import java.util.List;

import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.util.tree.TokenTree;

public interface TokenService {

    Token findById(Long tokenId);

    TokenTree findTokenTree(Long processInstanceId);

    List<Token> findChildTokensByParentTokenId(Long parentTokenId);

    List<Token> findChildTokensByProcessInstanceId(Long processInstanceId);

    List<Token> findInactiveTokensByActivityIdAndProcessInstanceId(String currentActivityId,
            Long processInstanceId);

    Token createProcessInstanceToken(ProcessDefinitionEntity processDefinition);

    Token createChildToken(Token processInstance);

    Token create();

    Token save(Token token);

    void updateProcessInstanceLockTime(Long processInstanceId);

    void clearProcessInstanceLockTime(Long processInstanceId);

    void deleteProcessInstanceToken(Long processInstanceId, String currentFlowElementId,
            String reason);

    void deleteChildTokens(Token token, String reason);

    void deleteTokenAndRelatedData(Token token, String reason);

    void deleteDataRelatedToToken(Token token, String reason);

    void delete(Token token);
}
