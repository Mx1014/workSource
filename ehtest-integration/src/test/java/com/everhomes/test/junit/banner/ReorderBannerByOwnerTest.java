// @formatter:off
package com.everhomes.test.junit.banner;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.ReorderBannerByOwnerCommand;
import com.everhomes.rest.banner.UpdateBannerByOwnerCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ReorderBannerByOwnerTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testReorderBanners() {
        Integer namespaceId = 2;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        byte scopeType = ScopeType.COMMUNITY.getCode();
        long scopeId = 0L;
        BannerScope scope = new BannerScope();
        scope.setScopeCode(scopeType);
        scope.setScopeId(scopeId);
        
        ReorderBannerByOwnerCommand cmd = new ReorderBannerByOwnerCommand();
        cmd.setOwnerType("organization");
        cmd.setOwnerId(1000750L);
        cmd.setScope(scope);
        
        List<UpdateBannerByOwnerCommand> upcs = new ArrayList<>();
        UpdateBannerByOwnerCommand ubbc = new UpdateBannerByOwnerCommand();
        ubbc.setDefaultOrder(1);
        ubbc.setId(1L);
        upcs.add(ubbc);
        
        ubbc = new UpdateBannerByOwnerCommand();
        ubbc.setDefaultOrder(2);
        ubbc.setId(2L);
        upcs.add(ubbc);
        
        ubbc = new UpdateBannerByOwnerCommand();
        ubbc.setDefaultOrder(3);
        ubbc.setId(3L);
        upcs.add(ubbc);
        
        ubbc = new UpdateBannerByOwnerCommand();
        ubbc.setDefaultOrder(4);
        ubbc.setId(4L);
        upcs.add(ubbc);
        
        cmd.setBanners(upcs);
        
        String commandRelativeUri = "/banner/reorderBannerByOwner";
        RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The banner should be created, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String jsonFilePath = "data/json/create-banner-by-owner-reorder-test-data.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}

