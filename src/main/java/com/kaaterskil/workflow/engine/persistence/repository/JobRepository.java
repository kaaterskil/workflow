package com.kaaterskil.workflow.engine.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {

    @Query(value = "select j from JobEntity j where j.tokenId = :tokenId")
    List<JobEntity> findByTokenId(@Param("tokenId") Long tokenId);

    @Query(value = "select j from JobEntity j where j.retries > 0 "
            + "and j.dueBy is not null and j.dueBy < :expirationTime "
            + "and (j.lockExpiresAt is null or j.lockExpiresAt < :expirationTime) "
            + "and j.tokenId is null")
    List<JobEntity> findAsyncJobsDueToExecute(@Param("expirationTime") Date expirationTime);

}