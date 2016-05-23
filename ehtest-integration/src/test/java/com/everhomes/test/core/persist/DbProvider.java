package com.everhomes.test.core.persist;

import org.jooq.DSLContext;

public interface DbProvider {
	DSLContext getDslContext();
	//void truncateAllTables();
	void runClassPathSqlFile(String filePath);
}
