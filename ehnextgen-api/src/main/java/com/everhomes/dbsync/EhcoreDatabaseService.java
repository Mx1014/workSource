package com.everhomes.dbsync;

import org.jooq.Field;
import org.jooq.Table;

public interface EhcoreDatabaseService {

    DataTable getTableMeta(String tableName);

    Field<?> getTableField(String name) throws Exception;

}
