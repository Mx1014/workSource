// @formatter:off
package com.everhomes.test.junit.community;

import static com.everhomes.server.schema.Tables.*;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.community.admin.UpdateCommunityAdminCommand;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class UpdateCommunityTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testUpdateCommunity() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/community/updateCommunity";
        
        UpdateCommunityAdminCommand cmd = new UpdateCommunityAdminCommand();
        cmd.setCommunityId(24210090697425925L);
        cmd.setAreaId(13910L);
        cmd.setCityId(13905L);
        cmd.setAreaSize(100D);
        cmd.setAddress("abc");
        
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhCommunities> result = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_COMMUNITIES)
            .where(EH_COMMUNITIES.ID.eq(24210090697425925L))
            .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, EhCommunities.class));
                return null;
            });
        
        assertEquals(1, result.size());
        assertEquals(100D, result.get(0).getAreaSize());
        assertEquals("abc", result.get(0).getAddress());
        
    }
    
   
    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
    	String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    	
        userInfoFilePath = "data/json/3.4.x-test-data-community_address_160605.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

