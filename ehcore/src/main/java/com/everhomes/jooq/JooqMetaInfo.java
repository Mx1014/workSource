// @formatter:off
package com.everhomes.jooq;

import org.jooq.Record;

/**
 * JooqMetaInfo is used to store JOOQ generated classes meta information
 *
 * @author Kelven Yang
 *
 */
public class JooqMetaInfo {
    private Class<?> recordClass;
    private Class<?> daoClass;
    private String tableName;
    
    public JooqMetaInfo() {
    }

    public Class<?> getRecordClass() {
        return recordClass;
    }

    public void setRecordClass(Class<?> recordClass) {
        this.recordClass = recordClass;
    }

    public Class<?> getDaoClass() {
        return daoClass;
    }

    public void setDaoClass(Class<?> daoClass) {
        this.daoClass = daoClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public Record getBlankRecordObject() {
        if(this.recordClass != null) {
            try {
                return (Record)this.recordClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                assert(false);
            }
        }
        
        return null;
    }
}
