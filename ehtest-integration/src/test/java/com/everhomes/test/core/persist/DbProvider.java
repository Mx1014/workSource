package com.everhomes.test.core.persist;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;

public interface DbProvider {
	DSLContext getDslContext();
	void truncateTable(Table<?> table);
	void truncateTable(List<Table<?>> tables);
	void truncateAllTables();
	void runClassPathSqlFile(String filePath);
	void runClassPathSqlFile(List<String> filePaths);
	void runClassPathSqlDirectory(String dirPath, String suffix);
	void runFileSystemSqlDirectory(String dirPath, String suffix);
	void runFileSystemSqlDirectory(List<String> dirPaths, String suffix);
	void runFileSystemSqlFile(String filePath);
	void runFileSystemSqlFile(List<String> filePaths);
	String getAbsolutePathFromClassPath(String classPath);
	void searchFileRecursively(List<String> resultList, File fromFile, String suffix);
	List<Map<String, Object>> convertRecordToMap(Table<?> table, Condition condtion);
	List<Map<String, Object>> convertRecordToMap(Result<?> records);
	void loadJsonFileToDatabase(String filePath, boolean truncateTableFirst);
}
