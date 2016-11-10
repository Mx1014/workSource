package com.everhomes.dbsync;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

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

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Ehcore;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
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
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private SyncDatabaseService syncDatabaseService;
    
    private Map<String, DataGraph> graphStorage;
    private Map<String, JSDataQueryObject> querybaseStorage;
    
    private String resourceRoot = "/dbsync/";
    
    public NashornObjectServiceImpl() {
        this.nobjs = new NashornObject[MAX];
        graphStorage = new ConcurrentHashMap<String, DataGraph>();
        querybaseStorage = new ConcurrentHashMap<String, JSDataQueryObject>();
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
        		if(name.indexOf('$') > 0) {
        			String[] ns = name.split("\\$");
        			if(ns.length != 3) {
        				return null;
        			}
        			
        			String mapping = null;
        			if(ns[0].equals("app")) {
        				mapping = syncDatabaseService.loadMapping(ns[1], ns[2]);
        			}
        			
        			if(mapping != null) {
        				if(mapping.endsWith(".js")) {
        					js = new ClassPathResource(resourceRoot + mapping);
        				} else {
        					js = new ClassPathResource(resourceRoot + "nothing.js");
        				}
        				
        				return js.getFile();
        			}
        		}
        		
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
        	
    		if(name.indexOf('$') > 0) {
    			String[] ns = name.split("\\$");
    			if(ns.length != 3) {
    				return null;
    			}
    			
    			String mapping = null;
    			if(ns[0].equals("app")) {
    				mapping = syncDatabaseService.loadMapping(ns[1], ns[2]);
    			}
    			
    			if(mapping != null) {
    				if(mapping.endsWith(".js")) {
    		         js = new ClassPathResource(resourceRoot + mapping);
    		         try(Scanner scanner = new Scanner(js.getInputStream())) {
    		                return scanner.useDelimiter("\\A").next();
    		            }
    				} else {
    					return mapping;
    				}
    			}
    		}
    		
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
    
    private DataGraph testGraph() {
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
        
        DataGraph graph3 = new DataGraph();
        GraphTable table3 = new GraphTable();
        table3.setTableName("eh_user_identifiers");
        table3.addField("identifier_token");
        table3.addField("id");
        table3.addField("claim_status");
        table3.addField("region_code");
        graph3.setTable(table3);
        
        GraphRefer hasMany = new GraphRefer();
        hasMany.setGraph(graph3);
        hasMany.setParentField("id");
        hasMany.setChildField("owner_uid");
        hasMany.setAsName("userIdentifiers");
        hasMany.setJoinType(NJoinType.NO_JOIN.getCode());
        graph2.addRefer(hasMany);
        
        return graph;        
    }
    
    @Override
    public DataGraph getGraph(String name) {
        if(name.equals("testGraph")) {
            return testGraph();
        }
        
        return graphStorage.get(name);
    }
    
    @Override
    public void saveGraph(DataGraph graph) {
        graphStorage.put(graph.getGraphName(), graph);
    }
    
    @Override
    public DataGraph createGraph(String graphJson) {
    	try {
    		DataGraph graph = DataGraph.fromJSDataGraph(graphJson);
        	if(graph == null) {
        		return null;
        	}
        	
        	saveGraph(graph);	
        	
        	return getGraph(graph.getGraphName());	
    	} catch(Exception ex) {
    		LOGGER.error("graph create failed", ex);
    	}
    	
    	return null;
    }
    
    @Override
    public List<Map<String, Object>> query(DatabaseQuery query) {
        query.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, query.getPageSize()));
        
        DatabaseQueryProcess process = new DatabaseQueryProcess(this, query);
        try {
            return process.processQuery();
        } catch (Exception e) {
            LOGGER.error("query failed", e);
        }
        return null;
    }
    
    @Override
    public Field<?> getTableField(String name) throws Exception {
        return ehcoreDatabaseService.getTableField(name);
    }
    
    @Override
    public void createQueryBase(String appName, String mapName, String jsonQuery) {
    	try {
    		JSDataQueryObject obj = (JSDataQueryObject)StringHelper.fromJsonString(jsonQuery, JSDataQueryObject.class);
    		obj.setName(appName + "$" + mapName);
    		querybaseStorage.put(obj.getName(), obj);
    	} catch(Exception ex) {
    		LOGGER.error("create query failed", ex);
    	}
    	
    }
    
    @Override
    public String makeQuery(String appName, String mapName, String queryUrl, String body) {
    	//TODO what if the input is binary?
    	String graphName = appName + "$" + mapName;
    	JSDataQueryObject queryObj = querybaseStorage.get(graphName);
    	DataGraph dataGraph = getGraph(graphName);
    	DatabaseQuery query = new DatabaseQuery();
    	
    	List<JSQueryObjectItem> condObjs = queryObj.get(queryUrl);
    	for(JSQueryObjectItem item : condObjs) {
    		if(item.getConditions() != null) {
    			for(String condStr : item.getConditions()) {
        			query.addCondition(item.getTableName(), condStr);	
        		}	
    		}
    		
    		if(item.getDefaults() != null) {
    			query.getInputs().putAll(item.getDefaults());
    		}
    	}
    	
    	//now write the real value
    	Map<String, String> inputs = (JSDataQueryInput)StringHelper.fromJsonString(body, JSDataQueryInput.class);
    	
    	query.getInputs().putAll(inputs);
    	query.setDataGraph(dataGraph);
    	
    	return StringHelper.toJsonString(this.query(query));
    }
}
