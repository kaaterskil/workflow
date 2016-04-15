package com.kaaterskil.workflow.engine.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kaaterskil.workflow.engine.persistence.entity.ActivityEntity;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    ActivityEntity findByKey(String activityId);

    @Query(value = "select a from ActivityEntity a where a.tokenId = :tokenId "
            + "and a.key = :activityId and a.endedAt is null")
    List<ActivityEntity> findUnfinishedActivities(@Param("tokenId") Long tokenId,
            @Param("activityId") String activityId);
}
