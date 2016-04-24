package com.kaaterskil.workflow.engine.persistence.repository;

import java.util.List;
import java.util.Set;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kaaterskil.workflow.engine.persistence.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "select e from Token e where e.processInstanceId = :processInstanceId "
            + "and e.parent is not null")
    List<Token> findChildTokensByProcessInstanceId(
            @Param("processInstanceId") Long processInstanceId);

    @Query(value = "select e from Token e where e.parent.id = :parentTokenId")
    List<Token> findByParentId(@Param("parentTokenId") Long parentTokenId);

    @Query(value = "select e from Token e where e.parent.id = :parentTokenId and e.currentActivity in :activityIds")
    List<Token> findByParentAndActivityIds(@Param("parentTokenId") Long parentTokenId,
            @Param("activityIds") Set<String> activityIds);

    @Query(value = "select e from Token e where e.currentActivity = :activityId and e.isActive = true")
    List<Token> findInactiveTokensByActivityId(@Param(":activityId") String activityId);

    @Query(value = "select e from Token e where e.currentActivity = :activityId "
            + "and e.processInstanceId = :processInstanceId and e.isActive = :isActive")
    List<Token> findInactiveTokensByActivityIdAndProcessInstanceId(
            @Param("activityId") String activityId,
            @Param("processInstanceId") Long processInstanceId,
            @Param("isActive") boolean isActive);

    @Query(value = "select e from Token e where e.rootProcessInstanceId = :rootProcessInstanceId")
    List<Token> findByRootProcessInstanceId(
            @Param("rootProcessInstanceId") Long rootProcessInstanceId);

    @Override
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Token findOne(Long id);
}
