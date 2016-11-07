package com.everhomes.dbsync;

import com.everhomes.util.StringHelper;

public class DataField {
    private String name;
    private DataType dataType;
    private String sqlType;
    private Boolean allowNull;
    private Boolean primaryKey;
    private Boolean autoIncrement;
    private Boolean isDefault;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getAllowNull() {
        return allowNull;
    }
    public void setAllowNull(Boolean allowNull) {
        this.allowNull = allowNull;
    }
    public Boolean getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
    public Boolean getAutoIncrement() {
        return autoIncrement;
    }
    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
    public DataType getDataType() {
        return dataType;
    }
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
    public String getSqlType() {
        return sqlType;
    }
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }
    public Boolean getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
