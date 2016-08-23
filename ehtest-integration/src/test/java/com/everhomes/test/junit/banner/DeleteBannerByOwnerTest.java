// @formatter:off
package com.everhomes.test.junit.banner;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.banner.DeleteBannerByOwnerCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.server.schema.Tables;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class DeleteBannerByOwnerTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void deleteBannerWithMultiScenes() {
    	Integer namespaceId = 2;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        DeleteBannerByOwnerCommand cmd = new DeleteBannerByOwnerCommand();
        cmd.setId(2L);
        cmd.setOwnerId(1000750L);
        cmd.setOwnerType("organization");
        BannerScope scope = new BannerScope();
        scope.setScopeCode(ScopeType.COMMUNITY.getCode());
        scope.setScopeId(24210090697426103L);
        cmd.setScope(scope);
        
        String commandRelativeUri = "/banner/deleteBannerByOwner";
        RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The banner should be created, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        Record record = dbProvider.getDslContext()
        		.select().from(Tables.EH_BANNERS)
        		.where(Tables.EH_BANNERS.STATUS.eq(BannerStatus.DELETE.getCode()))
        		.fetchOne();
        assertNotNull(record);
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String jsonFilePath = "data/json/create-banner-by-owner-create-test-data.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}

