package com.everhomes.dbsync;

import java.util.List;

import com.everhomes.util.StringHelper;

public class DataTable {
    private String tableName;
    private List<DataField> fields;
    private Class<?> dataClass;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DataField> getFields() {
        return fields;
    }

    public void setFields(List<DataField> fields) {
        this.fields = fields;
    }

    public Class<?> getDataClass() {
        return dataClass;
    }

    public void setDataClass(Class<?> dataClass) {
        this.dataClass = dataClass;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
