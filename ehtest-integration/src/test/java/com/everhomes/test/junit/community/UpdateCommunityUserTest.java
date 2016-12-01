// @formatter:off
package com.everhomes.test.junit.community;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.community.admin.UpdateCommunityUserCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class UpdateCommunityUserTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testUpdateCommunityUser() {
        Integer namespaceId = 0;
        String userIdentifier = "root";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/admin/community/updateCommunityUser";
        String IDNO = "5133232333";
        String position = "署理警务署长";
        UpdateCommunityUserCommand cmd = new UpdateCommunityUserCommand();
        cmd.setUserId(10001L);
        cmd.setExecutiveFlag(com.everhomes.rest.techpark.punch.NormalFlag.NO.getCode());
        cmd.setIdentityNumber(IDNO);
        cmd.setPosition(position);
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhUsers> result = new ArrayList<EhUsers>();
        DSLContext context = dbProvider.getDslContext();
        context.select().from(Tables.EH_USERS)
            .where(Tables.EH_USERS.ID.eq(10001L))
            .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, EhUsers.class));
                return null;
            });
        
        assertEquals(1, result.size());
        assertEquals(cmd.getExecutiveFlag().byteValue(), result.get(0).getExecutiveTag().byteValue());
        assertEquals(cmd.getPosition(),result.get(0).getPositionTag());
        assertEquals(cmd.getIdentityNumber(),result.get(0).getIdentityNumberTag()); 
        
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

