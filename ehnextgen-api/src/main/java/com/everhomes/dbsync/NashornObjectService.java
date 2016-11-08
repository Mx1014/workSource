package com.everhomes.dbsync;

import java.io.File;
import java.io.InputStream;

import org.jooq.Configuration;
import org.springframework.core.io.Resource;

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
    void queryTest();
    DataGraph getGraph(String name);
}
