package com.everhomes.dbsync;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;

public interface NashornObjectService {
    void put(NashornObject nobj) throws Exception;
    NashornObject get(long nid);
    NashornObject clear(long nid);
    Configuration configure(String tableName);
    String tableMeta(String tableName);
    Configuration configure();
    void log(String str);
    File getResource(String name);
    String getResourceAsStream(String name);
    DataTable getTableMeta(String tableName);
    DataGraph getGraph(String name);
    List<Map<String, Object>> query(DatabaseQuery query);
    Field<?> getTableField(String name) throws Exception;
    void saveGraph(DataGraph graph);
}
