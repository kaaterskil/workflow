package com.kaaterskil.workflow.engine.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kaaterskil.workflow.engine.persistence.entity.VariableEntity;

@Repository
public interface VariableRepository extends JpaRepository<VariableEntity, Long> {

    @Query(value = "select e from VariableEntity e where e.tokenId = :tokenId")
    List<VariableEntity> findByTokenId(@Param("tokenId") Long tokenId);

    VariableEntity findByName(String name);
}
