package com.kaaterskil.workflow.engine.persistence.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "TIMER")
public class TimerEntity extends JobEntity {

}
