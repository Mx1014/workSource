// @formatter: off
package com.everhomes.test.junit.banner;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.ListBannersByOwnerCommand;
import com.everhomes.rest.banner.ListBannersByOwnerRestResponse;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ListBannersByOwnerTest extends BaseLoginAuthTestCase{

	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testListBannersByOwner() {
		Integer namespaceId = 2;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        
        logon(namespaceId, userIdentifier, plainTexPassword);
        
		String commandRelativeUri = "/banner/listBannersByOwner";
		ListBannersByOwnerCommand cmd = new ListBannersByOwnerCommand();
		cmd.setOwnerType("organization");
	    cmd.setOwnerId(1000750L);
	    BannerScope scope = new BannerScope();
	    scope.setScopeCode(ScopeType.COMMUNITY.getCode());
	    scope.setScopeId(24210090697426103L);
	    cmd.setScope(scope);
		
	    ListBannersByOwnerRestResponse resp = httpClientService.restGet(commandRelativeUri, cmd, ListBannersByOwnerRestResponse.class, context);
		
		assertNotNull("The reponse of may not be null", resp);
	    assertTrue("The user scenes should be get from server, response=" + 
	            StringHelper.toJsonString(resp), httpClientService.isReponseSuccess(resp));
	    assertNotNull("The requests of may not be null", resp);
	    assertNotNull("The requests of may not be null", resp.getResponse());
	     
	    List<BannerDTO> banners = resp.getResponse().getBanners();
		assertEquals(5, banners.size());
	    banners.forEach(r -> {
	    	assertTrue(r.getPosterPath().startsWith("http://"));
	    });
	    
	    // 测试顺序
	    assertEquals(Long.valueOf(1), banners.get(0).getId());
	    assertEquals(Long.valueOf(5), banners.get(1).getId());
	    assertEquals(Long.valueOf(2), banners.get(2).getId());
	    assertEquals(Long.valueOf(4), banners.get(3).getId());
	    assertEquals(Long.valueOf(3), banners.get(4).getId());
	}
	
	protected void initCustomData() {
		String userInfoFilePath = "data/json/create-banner-by-owner-list-test-data.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
	
	@After
    public void tearDown() {
        logoff();
    }
}
