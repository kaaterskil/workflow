package com.kaaterskil.workflow.bpm.common.data;

public interface ItemAwareElement {

    String getItemSubjectRef();

    void setItemSubjectRef(String itemSubjectRef);

    DataState getDataState();

    void setDataState(DataState dataState);
}
