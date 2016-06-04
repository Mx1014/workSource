// @formatter:off
package com.everhomes.test.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.core.io.ClassPathResource;

import com.everhomes.rest.user.GetUserInfoRestResponse;
import com.everhomes.rest.user.LogonCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhNamespaceResources;
import com.everhomes.test.core.BaseServerTestCase;
import com.everhomes.test.core.UserContext;
import com.everhomes.test.core.http.HttpClientService;
import com.everhomes.test.core.persist.DbProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DbServiceTest extends BaseServerTestCase {
    
    @Autowired
    private DSLContext dbContext;
    
    @Autowired
    private DbProvider dbProvider; 
    
    private UserContext context;
    
    @Before
    public void setUp() {
    	String createTablefilePath = "data/tables/20160522_create_tables.sql";
    	long startTime = System.currentTimeMillis();
    	//dbProvider.truncateAllTables();
        //dbProvider.runClassPathSqlFile(createTablefilePath);
        long endTime = System.currentTimeMillis();
        System.out.println("run create tables sql file, elapse=" + (endTime - startTime));
        
        String truncateTablefilePath = "data/tables/20160522_truncate_tables.sql";
        startTime = System.currentTimeMillis();
        //dbProvider.runClassPathSqlFile(truncateTablefilePath);
        endTime = System.currentTimeMillis();
        System.out.println("run truncate tables sql file, elapse=" + (endTime - startTime));
    }
    
    @Test
    public void testGetUserInfo() {
    	ClassPathResource pathResource = new ClassPathResource("config");
    	try {
			String path = pathResource.getFile().getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	DSLContext dslContext = dbProvider.getDslContext();
    	EhNamespaceResources nsResource = dslContext.selectFrom(Tables.EH_NAMESPACE_RESOURCES).where(Tables.EH_NAMESPACE_RESOURCES.ID.eq(1L)).fetchAny().map((r) -> {
    		return ConvertHelper.convert(r, EhNamespaceResources.class);
    	});
    	//assertNotNull(nsResource);
    }
    
    @Ignore @Test
    public void testJsonFormate() {
    	DSLContext dslContext = dbProvider.getDslContext();
    	List<EhNamespaceResources> nsResources = new ArrayList<EhNamespaceResources>();
    	dslContext.selectFrom(Tables.EH_NAMESPACE_RESOURCES).where(Tables.EH_NAMESPACE_RESOURCES.ID.eq(1L)).fetch().map((r) -> {
    	    nsResources.add(ConvertHelper.convert(r, EhNamespaceResources.class));
    		return null;
    	});
    	
        //Map<String, String> nsResources = new HashMap<String, String>();
    	String uglyJSONString = StringHelper.toJsonString(nsResources);
    	System.out.println("uglyJSONString: " + uglyJSONString);
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);
    }
    
    @After
    public void tearDown() {
        
    }
}

