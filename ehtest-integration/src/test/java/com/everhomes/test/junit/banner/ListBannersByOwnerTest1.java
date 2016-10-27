// @formatter: off
package com.everhomes.test.junit.banner;

import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.ListBannersByOwnerCommand;
import com.everhomes.rest.banner.ListBannersByOwnerRestResponse;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ListBannersByOwnerTest1 extends BaseLoginAuthTestCase{

	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testListBannersByOwner1() {
		Integer namespaceId = 2;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        
        logon(namespaceId, userIdentifier, plainTexPassword);
        
		String commandRelativeUri = "/banner/listBannersByOwner";
		ListBannersByOwnerCommand cmd = new ListBannersByOwnerCommand();
		cmd.setOwnerType("organization");
	    cmd.setOwnerId(1000750L);
		cmd.setSceneType("default");

	    BannerScope scope = new BannerScope();
	    scope.setScopeCode(ScopeType.COMMUNITY.getCode());
	    scope.setScopeId(240111044331051500L);
	    cmd.setScope(scope);
		
	    ListBannersByOwnerRestResponse resp = httpClientService.restGet(commandRelativeUri, cmd, ListBannersByOwnerRestResponse.class, context);
		
		assertNotNull("The reponse of may not be null", resp);
	    assertTrue("The user scenes should be get from server, response=" + 
	            StringHelper.toJsonString(resp), httpClientService.isReponseSuccess(resp));
	    assertNotNull("The requests of may not be null", resp);
	    assertNotNull("The requests of may not be null", resp.getResponse());
	     
	    List<BannerDTO> banners = resp.getResponse().getBanners();
		assertEquals(6, banners.size());
	}
	
	protected void initCustomData() {
		String userInfoFilePath = "data/json/list-banner-by-owner-test-data.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
	
	@After
    public void tearDown() {
        logoff();
    }
}
