package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Field;
import org.jooq.Table;

import com.everhomes.util.StringHelper;

public class DataTable {
    private String tableName;
    private Table<?> tableJOOQ;
    private List<DataField> fields;
    private Map<String, Field<?>> fieldJOOQMap;
    
    public DataTable() {
        fieldJOOQMap = new HashMap<String, Field<?>>();
        fields = new ArrayList<DataField>();
    }

    public Table<?> getTableJOOQ() {
        return tableJOOQ;
    }

    public void setTableJOOQ(Table<?> tableJOOQ) {
        this.tableJOOQ = tableJOOQ;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DataField> getFields() {
        return fields;
    }

    public void putField(DataField df, Field<?> field) {
        this.fields.add(df);
        this.fieldJOOQMap.put(field.getName(), field);
    }
    
    public Field<?> getField(String name) {
        return this.fieldJOOQMap.get(name);
    }
    
    public Field getFieldObject(String name) {
        return this.fieldJOOQMap.get(name);
    }
    
    public List<Field<?>> getFields(List<String> names) {
        List<Field<?>> fields = new ArrayList<Field<?>>();
        for(String name : names) {
            Field<?> field = getField(name);
            if(field != null) {
                fields.add(field);    
            }
            
        }
        return fields;
    }
}
