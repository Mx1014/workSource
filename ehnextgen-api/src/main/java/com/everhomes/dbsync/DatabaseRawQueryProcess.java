package com.everhomes.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseRawQueryProcess {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRawQueryProcess.class);
	
	private NashornObjectService service;
	private String raw;
	private Map<String, String> inputs;
	private static final Pattern pParam = Pattern.compile("\\$\\w+");
	
	public DatabaseRawQueryProcess(NashornObjectService service, String raw, Map<String, String> inputs) {
		this.raw = raw;
		this.inputs = inputs;
		this.service = service;
	}
	
    private Result<Record> fetchSql(String sql, Map<String, String> inputs) throws Exception {
        //String sql = "eh_users.id = $id and eh_door_user_permission.user_id = $user_id;";
        String newSql = "";
        int start = 0;
        Matcher m = pParam.matcher(sql);
        List<Object> objs = new ArrayList<Object>();
        while(m.find()) {
            newSql += sql.substring(start, m.start());
            newSql += "?";
            start = m.end();
            String sub = sql.substring(m.start(), m.end());
            sub = sub.substring(1);
            if(!inputs.containsKey(sub)) {
                throw new Exception("sql binding error");
            }
            objs.add(inputs.get(sub));
            
        }
        
        if(start > 0) {
            newSql += sql.substring(start, sql.length());
        } else {
        	newSql = sql;
        }
        
        if(newSql.indexOf("limit") == -1) {
        	newSql += " limit 50 ";
        }
        
        if(objs.size() == 0) {
            return DSL.using(service.configure()).fetch(newSql);
        }
        
        LOGGER.info("typeof objs[0]=" + objs.get(0).getClass());
        
        return DSL.using(service.configure()).fetch(newSql, objs.toArray());
    }
	
    public static List<Map<String, Object>> processRecord(Result<Record> records) {
        int size = records.size();
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        
        for(int i = 0; i < size; i++) {
            Record r = records.get(i);
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> obj = null;
            
            String currTable = "";
            int j = 0;
            
            Field<?>[] fields = r.fields();
            for(Field<?> field: fields) {
            	String fieldStr = field.toString();
            	String[] fs = fieldStr.split("\\.");
            	String tableName = null;
            	String fieldName = "";
            	if(fs.length == 3) {
            		tableName = fs[1];
            		fieldName = fs[2];
            	} else if(fs.length == 2) {
            		tableName = fs[0];
            		fieldName = fs[1];
            	} else if(fs.length == 1) {
            		currTable = "";
            		fieldName = fieldStr;
            		Object o = r.getValue(j);
            		if(o != null) {
            			result.put(fieldStr, r.getValue(j));	
            		}
            		
            		j++;
            		continue;
            	}
            	
            	
            	if(!currTable.equals(tableName)) {
            		obj = new HashMap<String, Object>();
            		result.put(tableName, obj);
            		currTable = tableName;
            	}
            	
            	Object o = r.getValue(j);
            	if(o != null) {
            		obj.put(fieldName, r.getValue(j));
            		}
            	
            	j++;
            }
            
            results.add(result);
            
            }
        
        return results;
    }
    
	public List<Map<String, Object>> processQuery() throws Exception {
		Result<Record> records = fetchSql(raw, inputs);
		return processRecord(records);
	}
}
