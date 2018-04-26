package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.util.StringHelper;

public class GraphTable {
    private String tableName;
    private List<String> fields;
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
        this.fields = new ArrayList<String>();
    }
    public List<String> getFields() {
        return fields;
    }
    public void setFields(List<String> fields) {
        this.fields = fields;
    }
    public void addField(String field) {
        this.fields.add(field);
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
