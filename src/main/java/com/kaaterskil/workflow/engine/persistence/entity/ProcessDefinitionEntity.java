package com.kaaterskil.workflow.engine.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "wf_def_process_definitions",
       uniqueConstraints = { @UniqueConstraint(columnNames = { "version", "key" }) })
public class ProcessDefinitionEntity {

    @Id
    @SequenceGenerator(name = "wf_def_process_definitions_id_seq_gen",
                       sequenceName = "wf_def_process_definitions_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "wf_def_process_definitions_id_seq_gen")
    @Column(name = "process_definition_id")
    private Long id;

    @Column(name = "key", length = 255, nullable = false)
    private String key;

    @Column(name = "deployment_id")
    private Long deploymentId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "category", length = 255)
    private String category;

    @Column(name = "version", nullable = false)
    private int version;

    @Override
    public String toString() {
        return String.format(
                "ProcessDefinitionEntity [id=%s, key=%s, deploymentId=%s, name=%s, description=%s, category=%s, version=%s]",
                id, key, deploymentId, name, description, category, version);
    }

    /*---------- Getter/Setters ----------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(Long deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
