package com.everhomes.dbsync;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectOnStep;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Ehcore;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.StringHelper;

@Component
public class NashornObjectServiceImpl implements NashornObjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NashornObjectServiceImpl.class);
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private EhcoreDatabaseService ehcoreDatabaseService;
    
    private NashornObject[] nobjs;      //TODO use CocurrentHashmap instead ?
    private final int MAX = 1024*10;    //10K
    
    @Autowired
    private DbProvider dbProvider;
    
    private String resourceRoot = "/dbsync/";
    
    public NashornObjectServiceImpl() {
        this.nobjs = new NashornObject[MAX];
    }
    
    @Override
    public void put(NashornObject nobj) throws Exception {
        long nid = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(NashornObjectServiceImpl.class));
        int id = (int)(nid % MAX);
        NashornObject old = null;
        long now = System.currentTimeMillis() - nobj.getTimeout();
        boolean exist = false;
        
        synchronized (this) {
            old = this.nobjs[id];
            if(old != null && now < old.getCreateTime()) {
                //the old no timeout
                exist = true;
            }
            
            if(exist) {
                throw new Exception(" old key not timeout ");
            }
            
            nobj.setId(nid);
            synchronized (this) {
                this.nobjs[id] = nobj;
                }
        }
    }

    @Override
    public NashornObject clear(long nid) {
        int id = (int)(nid % MAX);
        synchronized (this) {
            NashornObject obj = this.nobjs[id];
            this.nobjs[id] = null;
            return obj;
        }
    }
    
    @Override
    public NashornObject get(long nid) {
        int id = (int)(nid % MAX);
        synchronized (this) {
            NashornObject obj = this.nobjs[id];
            return obj;
        }
    }

    @Override
    public Configuration configure() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return context.configuration();
    }
    
    @Override
    public Configuration configure(String tableName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return context.configuration();
    }
    
    @Override
    public String tableMeta(String tableName) {
        Table<?> table = Ehcore.EHCORE.getTable(tableName);
        String origin = "";
        for(Field<?> field : table.fields()) {
            DataType<?> dataType = field.getDataType();
            origin += " " + dataType.getTypeName();
        }
        
        return origin;
    }
    
    @Override
    public File getResource(String name) {
        Resource js = null;
        try {
            js = new ClassPathResource(resourceRoot + name);
            return js.getFile();
        } catch(Exception ex) {
            LOGGER.info("get resource failed name=" + name, ex);
        }
         
        return null;
    }
    
    @Override
    public String getResourceAsStream(String name) {
        Resource js = null;
        try {
            js = new ClassPathResource(resourceRoot + name);
            try(Scanner scanner = new Scanner(js.getInputStream())) {
                return scanner.useDelimiter("\\A").next();
            }
            
        } catch(Exception ex) {
            LOGGER.info("get resource failed file=" + name, ex);
        }
         
        return null;        
    }
    
    @Override
    public DataTable getTableMeta(String tableName) {
        return ehcoreDatabaseService.getTableMeta(tableName);
    }
    
    @Override
    public void log(String str) {
        LOGGER.info("from js: " + str);
    }
    
    @Override
    public DataGraph getGraph(String name) {
        DataGraph graph = new DataGraph();
        GraphTable table1 = new GraphTable();
        table1.setTableName("eh_door_user_permission");
        table1.addField("id");
        table1.addField("user_id");
        table1.addField("namespace_id");
        graph.setTable(table1);
        
        GraphTable table2 = new GraphTable();
        table2.setTableName("eh_users");
        table2.addField("uuid");
        table2.addField("id");
        table2.addField("nick_name");
        
        DataGraph graph2 = new DataGraph();
        graph2.setTable(table2);
        
        GraphRefer belong = new GraphRefer();
        belong.setGraph(graph2);
        belong.setParentField("user_id");
        belong.setChildField("id");
        belong.setAsName("user");
        belong.setJoinType(NJoinType.INNER_JOIN.getCode());
        
        graph.addRefer(belong);
        
        return graph;
    }
    
    @Override
    public Result<Record> query(DatabaseQuery query) {
        DatabaseQueryProcess process = new DatabaseQueryProcess(this, query);
        try {
            Result<Record> records = process.processQuery();
            return records;
        } catch (Exception e) {
            LOGGER.error("query failed", e);
        }
        return null;
    }
    
    @Override
    public Field<?> getTableField(String name) throws Exception {
        return ehcoreDatabaseService.getTableField(name);
    }
}
