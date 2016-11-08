package com.everhomes.dbsync;

import org.jooq.Table;

public interface EhcoreDatabaseService {

    DataTable getTableMeta(String tableName);

}
