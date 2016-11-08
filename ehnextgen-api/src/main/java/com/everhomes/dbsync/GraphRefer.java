package com.everhomes.dbsync;

import java.util.List;

import com.everhomes.util.StringHelper;

public class GraphRefer {
    private DataGraph graph;
    private String parentField;
    private String childField;
    private String asName;
    private String joinType;

    public DataGraph getGraph() {
        return graph;
    }

    public void setGraph(DataGraph graph) {
        this.graph = graph;
    }

    public String getParentField() {
        return parentField;
    }

    public void setParentField(String parentField) {
        this.parentField = parentField;
    }

    public String getChildField() {
        return childField;
    }

    public void setChildField(String childField) {
        this.childField = childField;
    }

    public String getAsName() {
        return asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
