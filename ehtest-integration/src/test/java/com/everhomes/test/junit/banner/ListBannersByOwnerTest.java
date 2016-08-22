// @formatter: off
package com.everhomes.test.junit.banner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.banner.ListBannersByOwnerCommand;
import com.everhomes.rest.banner.ListBannersByOwnerCommandResponse;
import com.everhomes.rest.banner.admin.BannerListBannersRestResponse;
import com.everhomes.rest.banner.admin.ListBannersAdminCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ListBannersByOwnerTest extends BaseLoginAuthTestCase{

	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testListBanners() {
		Integer namespaceId = 0;
        String userIdentifier = "12000000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        logon(namespaceId, userIdentifier, plainTexPassword);
        
		String commandRelativeUri = "/banner/listBannersByOwner";
		ListBannersByOwnerCommand cmd = new ListBannersByOwnerCommand();
		cmd.setOwnerType("organization");
	    cmd.setOwnerId(1000750L);
	    cmd.setCommunityId(24210090697426103L);
		
	    RestResponseBase resp = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class, context);
		
		assertNotNull("The reponse of may not be null", resp);
	    assertTrue("The user scenes should be get from server, response=" + 
	            StringHelper.toJsonString(resp), httpClientService.isReponseSuccess(resp));
	    assertNotNull("The requests of may not be null", resp);
	     
	   // assertEquals(1, ((ListBannersByOwnerCommandResponse)resp.getResponseObject()).getBanners());
	}
	
	protected void initCustomData() {
		String userInfoFilePath = "data/json/create-banner-by-owner-test-data.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
	
	@After
    public void tearDown() {
        logoff();
    }
}
