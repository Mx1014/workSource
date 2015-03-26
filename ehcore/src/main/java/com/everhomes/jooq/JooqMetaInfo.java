// @formatter:off
package com.everhomes.jooq;

import org.jooq.Record;
import org.jooq.impl.TableImpl;

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
    private TableImpl tableImpl; 
    
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
    
    @SuppressWarnings("rawtypes")
    public TableImpl getTableImpl() {
        return this.tableImpl;
    }
    
    @SuppressWarnings("rawtypes")
    public void setTableImpl(TableImpl tableImpl) {
        this.tableImpl = tableImpl;
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
