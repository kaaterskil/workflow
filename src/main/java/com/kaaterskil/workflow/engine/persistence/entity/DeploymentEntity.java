package com.kaaterskil.workflow.engine.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "wf_def_deployments")
public class DeploymentEntity {

    @Id
    @SequenceGenerator(name = "wf_def_deployments_id_seq_gen",
                       sequenceName = "wf_def_deployments_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wf_def_deployments_id_seq_gen")
    @Column(name = "deployment_id")
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "category", length = 255)
    private String category;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deployed_at")
    private Date deployedAt;

    @Transient
    private boolean isNew;

    /*---------- Getter/Setters ----------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDeployedAt() {
        return deployedAt;
    }

    public void setDeployedAt(Date deployedAt) {
        this.deployedAt = deployedAt;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}
