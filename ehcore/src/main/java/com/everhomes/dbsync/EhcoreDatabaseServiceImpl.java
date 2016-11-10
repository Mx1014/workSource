package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Ehcore;
import com.everhomes.server.schema.Keys;
import com.everhomes.server.schema.Tables;

@Component
public class EhcoreDatabaseServiceImpl implements EhcoreDatabaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EhcoreDatabaseServiceImpl.class);
    private Map<String, DataTable> tableMap;
    
    public EhcoreDatabaseServiceImpl() {
        tableMap = new HashMap<String, DataTable>();
        
        init();
    }
    
    private void init() {
        List<Table<?>> tables = Ehcore.EHCORE.getTables();
        for(Table<?> table : tables) {
            DataTable dt = new DataTable();
            generateDT(dt, table);
            tableMap.put(table.getName(), dt);
        }
    }
    
    private void generateDT(DataTable dt, Table<?> table) {
        dt.setTableName(table.getName());
        dt.setTableJOOQ(table);
        
        Field<?>[] fields = table.fields();
        for(Field<?> field : fields) {
            DataField df = new DataField();
            if(generateDF(df, field)) {
                dt.putField(df, field);
            }
        }
    }
    
    private boolean generateDF(DataField df, Field<?> field) {
        DataType<?> dataType = field.getDataType();
        df.setAllowNull(dataType.nullable());
        df.setIsDefault(dataType.defaulted());
        df.setName(field.getName());
        df.setSqlType(dataType.toString());
        df.setComment(field.getComment());
        if(dataType.isNumeric()) {
            df.setDataType(NDataType.INTEGER);
        } else if(dataType.isLob() || dataType.isBinary()) {
            df.setDataType(NDataType.BINARY);
        } else if(dataType.isString()) {
            df.setDataType(NDataType.STRING);
        } else if(dataType.isDateTime()) {
            df.setDataType(NDataType.DATETIME);
        } else {
            LOGGER.warn("unsupport field, name=" + field.getName());
            return false;
        }
        
        return true;
    }
    
    @Override
    public DataTable getTableMeta(String tableName) {
        return tableMap.get(tableName);
    }
    
    @Override
    public Field<?> getTableField(String name) throws Exception {
        String tableName = null;
        String fieldName = null;
        String[] ns = name.split("\\.");
        if(ns.length == 3) {
            tableName = ns[1].trim();
            fieldName = ns[2].trim();
        } else if(ns.length == 2) {
            tableName = ns[0].trim();
            fieldName = ns[1].trim();            
        } else {
            throw new Exception("field=" + name + " not found!");
        }
        
        DataTable dt = getTableMeta(tableName);
        if(dt == null) {
            throw new Exception("tableName=" + tableName + " not found");
        }
        
        return dt.getField(fieldName);
    }
}
