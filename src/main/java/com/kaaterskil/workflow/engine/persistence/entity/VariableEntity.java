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

import com.kaaterskil.workflow.engine.variable.VariableAccessors;
import com.kaaterskil.workflow.engine.variable.VariableType;

@Entity
@Table(name = "wf_run_variables")
public class VariableEntity implements VariableAccessors {

    @Id
    @SequenceGenerator(name = "wf_run_variables_id_seq_gen",
                       sequenceName = "wf_run_variables_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wf_run_variables_id_seq_gen")
    @Column(name = "variable_id")
    private Long id;

    @Column(name = "process_instance_id")
    private Long processInstanceId;

    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "value_type", length = 100)
    private String valueType;

    @Column(name = "boolean_value")
    private Boolean booleanValue;

    @Column(name = "int_value")
    private Integer intValue;

    @Column(name = "long_value")
    private Long longValue;

    @Column(name = "float_value")
    private Float floatValue;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "byte_value")
    private byte[] byteValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /*---------- Instance properties ----------*/

    @Transient
    private VariableType type;

    @Transient
    private Object cachedValue;

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }

    public Object getCachedValue() {
        return cachedValue;
    }

    public void setCachedValue(Object cachedValue) {
        this.cachedValue = cachedValue;
    }

    /*---------- Methods ----------*/

    public Object getValue() {
        if(cachedValue == null) {
            cachedValue = type.getValue(this);
        }
        return cachedValue;
    }

    public void setValue(Object value) {
        type.setValue(value, this);
        valueType = type.getType();
        cachedValue = value;
    }

    /*---------- Getter/Setters ----------*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @Override
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    @Override
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    @Override
    public Integer getIntValue() {
        return intValue;
    }

    @Override
    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    @Override
    public Long getLongValue() {
        return longValue;
    }

    @Override
    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    @Override
    public Float getFloatValue() {
        return floatValue;
    }

    @Override
    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }

    @Override
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public byte[] getByteValue() {
        return byteValue;
    }

    @Override
    public void setByteValue(byte[] byteValue) {
        this.byteValue = byteValue;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
