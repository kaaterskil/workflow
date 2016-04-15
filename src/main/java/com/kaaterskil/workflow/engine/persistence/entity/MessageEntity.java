package com.kaaterskil.workflow.engine.persistence.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "MESSAGE")
public class MessageEntity extends JobEntity {

}
