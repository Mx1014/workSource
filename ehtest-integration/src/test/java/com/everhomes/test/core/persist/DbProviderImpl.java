package com.everhomes.test.core.persist;

import com.everhomes.test.core.util.GsonHelper;
import com.google.gson.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class DbProviderImpl implements DbProvider {
    public static final String CALL_FUN_PREFIX = "callfun(";
    public static final String CALL_FUN_SUFFIX = ")";
    
    /** 服务器地址 */
    @Value("${db.mapping_name:ehcore_test}")
    private String dbMappingName;
    
    @Value("${db.exclude_tables:}")
    private String excludeTableString;
    
    @Autowired
    private DSLContext dslContext;
    
    @Autowired
    private DataSource dataSource;

	public DSLContext getDslContext() {
		return this.dslContext;
	}
	
	@Override
    public void truncateTable(Table<?> table) {
	    this.dslContext.execute("SET foreign_key_checks = 0");
	    this.dslContext.truncate(table).execute();
	    this.dslContext.execute("SET foreign_key_checks = 1");
	}
    
    @Override
    public void truncateTable(List<Table<?>> tables) {
        for(Table<?> table : tables) {
            truncateTable(table);
        }
    }
	
	@Override
	public void truncateAllTables() {
//		this.dslContext.meta().getTables().stream().map((r) -> {
//		    if("eh_acl_privileges".equals(r.getName())) {
//		        System.out.println();
//		    }
//			this.dslContext.truncate(r).execute();
//			return null;
//		});
	    
	    List<String> excludeTables = new ArrayList<String>();
	    if(excludeTableString != null && excludeTableString.trim().length() > 0) {
	        String[] segments = excludeTableString.split(",");
	        for(String segment : segments) {
	            segment = segment.trim();
	            if(segment.length() > 0) {
	                excludeTables.add(segment);
	            }
	        }
	    }
	    
	    // For InnoDB tables, the row count is only a rough estimate used in SQL optimization. 
	    // 即在InnoDB中，从information_schema数据库中读取tables表得到的rowcount是一个大致值，并不准确的的，
	    // 这会导致有的时候测试用例由于导入数据仍然存在则报错 by lqs 20161102
//	    List<Table<?>> tables = this.dslContext.select(org.jooq.util.mysql.information_schema.tables.Tables.TABLE_SCHEMA,
//	            org.jooq.util.mysql.information_schema.tables.Tables.TABLE_NAME,
//	            org.jooq.util.mysql.information_schema.tables.Tables.TABLE_ROWS)
//	        .from(org.jooq.util.mysql.information_schema.Tables.TABLES)
//	        .where(org.jooq.util.mysql.information_schema.tables.Tables.TABLE_SCHEMA.in(dbMappingName))
//	        .fetch().stream().map((r) -> {
//	            String tableName = r.getValue(org.jooq.util.mysql.information_schema.tables.Tables.TABLE_NAME);
//	            long rowCount = r.getValue(org.jooq.util.mysql.information_schema.tables.Tables.TABLE_ROWS).longValue();
//	            if(!excludeTables.contains(tableName) && rowCount > 0) {
//	                return DSL.table(tableName);
//	            } else {
//	                return null;
//	            }
//	        }).filter(Objects::nonNull).collect(Collectors.toList());
	    
//	    String tableName = "";
//	    Result<Record> x = this.dslContext.fetch("SELECT EXISTS(SELECT 1 FROM " + tableName + ")");
//        if(x.size() == 0 || x.get(0).getValue(0)) {
//            
//        }
	    
	    // 改用每张表都查询的方式，但为了减少时间，使用SELECT EXISTS(SELECT 1 FROM table_name)的方式，如果表有行则返回1、否则返回0；
	    // 这样查询时间也比较短； by lqs 20161102
	    List<Table<?>> tables = this.dslContext.select(org.jooq.util.mysql.information_schema.tables.Tables.TABLE_SCHEMA,
               org.jooq.util.mysql.information_schema.tables.Tables.TABLE_NAME)
           .from(org.jooq.util.mysql.information_schema.Tables.TABLES)
           .where(org.jooq.util.mysql.information_schema.tables.Tables.TABLE_SCHEMA.in(dbMappingName))
           .fetch().stream().map((r) -> {
               String tableName = r.getValue(org.jooq.util.mysql.information_schema.tables.Tables.TABLE_NAME);
               Result<Record> x = this.dslContext.fetch("SELECT EXISTS(SELECT 1 FROM " + tableName + ")");
               if(x.size() != 0 && !x.get(0).getValue(0).equals(0L) && !excludeTables.contains(tableName)) {
                   return DSL.table(tableName);
               } else {
                   return null;
               }
           }).filter(Objects::nonNull).collect(Collectors.toList());
	    
	    truncateTable(tables);
	}
	
	@Override
	public void runClassPathSqlFile(String filePath) {
		ClassPathResource resource = new ClassPathResource(filePath);
		
		try {
			runFileSystemSqlFile(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public void runClassPathSqlFile(List<String> filePaths) {
	    if(filePaths == null) {
            throw new IllegalArgumentException("The file path list may not be null");
        }
	    
	    for(String filePath : filePaths) {
	        runClassPathSqlFile(filePath);
	    }
	}
	
	@Override
	public void runClassPathSqlDirectory(String dirPath, String suffix) {
	    ClassPathResource resource = new ClassPathResource(dirPath);
	    try {
            List<String> sqlFileList = new ArrayList<String>();
            searchFileRecursively(sqlFileList, resource.getFile(), suffix);
            
            runFileSystemSqlFile(sqlFileList);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to run sql file, dirPath=" + dirPath, e);
        }
	}
	
	@Override
	public void runFileSystemSqlDirectory(String dirPath, String suffix) {
		if(dirPath == null) {
			throw new IllegalArgumentException("The directory path may not be null");
		}
		
		List<String> dirPaths = new ArrayList<String>();
		dirPaths.add(dirPath);
		runFileSystemSqlDirectory(dirPaths, suffix);
	}
	
	@Override
	public void runFileSystemSqlDirectory(List<String> dirPaths, String suffix) {
		if(dirPaths == null) {
			throw new IllegalArgumentException("The directory path list may not be null");
		}
		
		List<String> sqlFileList = new ArrayList<String>();
		for(String dirPath : dirPaths) {
			File dir = new File(dirPath);
			if(!dir.exists()) {
				throw new IllegalArgumentException("The directory not exist, path=" + dir.getAbsolutePath());
			}
			
			if(!dir.isDirectory()) {
				throw new IllegalArgumentException("The path is not a directory, path=" + dir.getAbsolutePath());
			}
			
			searchFileRecursively(sqlFileList, dir, suffix);
		}
		
		runFileSystemSqlFile(sqlFileList);
	}
	
	@Override
	public void runFileSystemSqlFile(String filePath) {
		List<String> filePaths = new ArrayList<String>();
		filePaths.add(filePath);
		
		runFileSystemSqlFile(filePaths);
	}
    
	@Override
	public void runFileSystemSqlFile(List<String> filePaths) {
		if(filePaths == null) {
			throw new IllegalArgumentException("The file path list may not be null");
		}
		
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		for (String filePath : filePaths) {
			FileSystemResource resource = new FileSystemResource(filePath);
			populator.addScript(resource);
		}
		
		DatabasePopulatorUtils.execute(populator, this.dataSource);
//		Runtime runtime=Runtime.getRuntime();
//		try{
//			runtime.exec("mysqldump  --no-defaults -uroot -p Pbgpwd#123 ehcore < E:/WorkPlace/Java/J2SE/ehtest-integration/src/test/data/tables/20160522_create_tables.sql");
//		} catch(Exception e) {
//			throw new IllegalStateException(e);
//		}
	}
	
	@Override
	public String getAbsolutePathFromClassPath(String classPath) {
        try {
            ClassPathResource resource = new ClassPathResource(classPath);
            return resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to run sql file, filePath=" + classPath, e);
        }
	}
	
	@Override
	public void searchFileRecursively(List<String> resultList, File fromFile, String suffix) {
		if(resultList == null) {
			throw new IllegalArgumentException("The result list may not be null");
		}
		
		if(fromFile.isDirectory()) {
			File[] subFiles = fromFile.listFiles();
			for(File subFile : subFiles) {
				searchFileRecursively(resultList, subFile, suffix);
			}
		} else {
			if(suffix == null || suffix.trim().length() == 0 
					|| fromFile.getName().toLowerCase().endsWith(suffix.toLowerCase())) {
				resultList.add(fromFile.getAbsolutePath());
			}
		}
	}
    
    @Override
    public List<Map<String, Object>> convertRecordToMap(Table<?> table, Condition condtion) {
        SelectQuery<?> query = dslContext.selectQuery(table);
        if(condtion != null) {
            query.addConditions(condtion);
        }
        
        Result<?> records = query.fetch();
        return convertRecordToMap(records);
    }
	
	@Override
	public List<Map<String, Object>> convertRecordToMap(Result<?> records) {
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        if(records == null) {
            return list;
        }
        
        for (Record record : records) {
            Map<String,Object> map = new HashMap<String, Object>();
            for(int i = 0; i < record.fields().length; i++) {
                Field<?> field = record.fields()[i];
                Object obj = record.getValue(i);
                String value = null;
                if(obj != null) {
                    value = obj.toString();
                    if(obj instanceof Timestamp) {
                        Timestamp timeObj = (Timestamp)obj;
                        value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeObj.getTime()));
                    }
                    if(obj instanceof java.sql.Date) {
                        java.sql.Date dateObj = (java.sql.Date)obj;
                        value = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dateObj.getTime()));
                    }
                }
                map.put(field.getName(), value);
            }
            list.add(map);
        }
        
        return list;
    }
	
	@Override
	public void loadJsonFileToDatabase(String filePath, boolean truncateTableFirst) {
	    if(filePath == null) {
            throw new IllegalArgumentException("The file path may not be null");
        }
	    
	    String jsonString = GsonHelper.readerTextFile(filePath);
	    
	    // 记录出现的表，用于在导数据之前先清除表数据
	    List<Table<?>> tableList = new ArrayList<Table<?>>();
	    
	    if(jsonString.trim().length() > 0) {
	        List<Query> recordInsertList = new ArrayList<Query>();
            JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
            Iterator<Entry<String, JsonElement>> tableIterator = jsonObject.entrySet().iterator();
            while(tableIterator.hasNext()) {
                Entry<String, JsonElement> tableEntry = tableIterator.next();
                
                // 从表名称转为表对象
                String tableName = tableEntry.getKey();
                Table<Record> table = DSL.tableByName(tableName);
                tableList.add(table);
                
                // 把每个json对象转为一行可插入的对象，其中包含表字段及对应的值
                JsonElement jsonRecords = tableEntry.getValue();
                if(jsonRecords.isJsonArray()) {
                    JsonArray records = jsonRecords.getAsJsonArray();
                    records.forEach((jsonRecord) -> {
                        recordInsertList.add(convertJsonRecordToInsertInfo(table, jsonRecord));
                    });
                } else {
                    recordInsertList.add(convertJsonRecordToInsertInfo(table, jsonRecords));
                }
            }
            
            if(truncateTableFirst) {
                truncateTable(tableList);
            }

            dslContext.batch(recordInsertList).execute();
	    }
	}
	
	private Query convertJsonRecordToInsertInfo(Table<Record> table, JsonElement jsonRecord) {
	    JsonObject record = jsonRecord.getAsJsonObject();
        List<Field<Object>> colunNames = new ArrayList<Field<Object>>();
        List<String> colunValues = new ArrayList<String>();
        Iterator<Entry<String, JsonElement>> recordIterator = record.entrySet().iterator();
        while(recordIterator.hasNext()) {
            Entry<String, JsonElement> recordEntry = recordIterator.next();
            String columnName = recordEntry.getKey();
            colunNames.add(DSL.fieldByName(columnName));
			JsonElement value = recordEntry.getValue();
			String columnValue = null;
			if (!(value instanceof JsonNull)) {
				columnValue = processValue(value.getAsString());
			}
			colunValues.add(columnValue);
		}
        
        return dslContext.insertInto(table, colunNames).values(colunValues);
	}
	
	private String processValue(String originalValue) {
	    if(originalValue == null || originalValue.trim().length() == 0) {
	        return originalValue;
	    }
	    
	    if(originalValue.startsWith(CALL_FUN_PREFIX) && originalValue.endsWith(CALL_FUN_SUFFIX)) {
	        String funStr = originalValue.substring(CALL_FUN_PREFIX.length(), originalValue.length() - CALL_FUN_SUFFIX.length());
	        int pos = funStr.indexOf(':');
	        if(pos != -1) {
	            String className = funStr.substring(0, pos);
	            String methodName = funStr.substring(pos + 1);
	            
	            try {
                    Object obj = Class.forName(className).getDeclaredMethod(methodName).invoke(null);
                    originalValue = obj.toString();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid call function in value, value=" + originalValue, e);
                } 
	        }
	    }
	    
	    return originalValue;
	}
}
