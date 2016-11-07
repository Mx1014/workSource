package com.everhomes.dbsync;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Ehcore;
import com.everhomes.server.schema.Keys;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.StringHelper;

@Component
public class NashornObjectServiceImpl implements NashornObjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NashornObjectServiceImpl.class);
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
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
    public void log(String str) {
        LOGGER.info("from js: " + str);
    }
}
