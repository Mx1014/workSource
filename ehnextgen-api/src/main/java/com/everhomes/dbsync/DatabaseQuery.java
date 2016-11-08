package com.everhomes.dbsync;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class DatabaseQuery {
    private String name;
    private String graphName;
    private List<String> conditions;
    private Map<String, String> inputs;
    private List<String> orders;
    
    private Long pageAnchor;
    private Integer pageSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    public Map<String, String> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, String> inputs) {
        this.inputs = inputs;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }
    
    public DatabaseQuery newCopy() {
        String json = StringHelper.toJsonString(this);
        return (DatabaseQuery)StringHelper.fromJsonString(json, DatabaseQuery.class);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
