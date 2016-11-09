package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class DatabaseQuery {
    private Map<String, List<String>> conditions;
    private Map<String, String> inputs;
    private List<String> orders;
    private DataGraph dataGraph;
    
    private Long pageAnchor;
    private Integer pageSize;
    
    public DatabaseQuery() {
        this.conditions = new HashMap<String, List<String>>();
        this.inputs = new HashMap<String, String>();
        this.orders = new ArrayList<String>();
    }

    public DataGraph getDataGraph() {
        return dataGraph;
    }

    public void setDataGraph(DataGraph dataGraph) {
        this.dataGraph = dataGraph;
    }

    public List<String> getConditions(String tableName) {
        List<String> lst = conditions.getOrDefault(tableName, new ArrayList<String>());
        if(lst.size() == 0) {
            //save this reference first
            conditions.put(tableName, lst);
        }
        
        return lst;
    }
    
    public void addCondition(String tableName, String condition) {
        List<String> lst = conditions.getOrDefault(tableName, new ArrayList<String>());
        if(lst.size() == 0) {
            conditions.put(tableName, lst);
        }
        lst.add(condition);
    }

    public Map<String, String> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, String> inputs) {
        this.inputs = inputs;
    }
    
    public void putInput(String key, String value) {
        this.inputs.put(key, value);
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
}
