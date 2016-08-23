// @formatter:off
package com.everhomes.test.junit.banner;

import static com.everhomes.server.schema.Tables.EH_BANNERS;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.banner.UpdateBannerByOwnerCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.server.schema.tables.pojos.EhBanners;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class UpdateBannerByOwnerTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testUpdateBannerByOwner() {
    	List<EhBanners> result = new ArrayList<>();
    	DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_BANNERS)
            .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, EhBanners.class));
                return null;
            });
        assertEquals(3, result.size());
        
        Integer namespaceId = 2;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String name = "Update banner name";
        String posterUri = "cs://1/image/aW1hZ2UvTVRvM1ltRm1ORE01WVRNeU56UmtZakZqWVRRME1tVmhabUpqT0RNMU1qazRNUQ";
        byte actionType = 9;
        String actionData = "{\"url\":\"www.zzll.com\"}"; 
        int order = 10;
        byte status = BannerStatus.ACTIVE.getCode();

        byte scopeType = ScopeType.COMMUNITY.getCode();
        long scopeId = 1122L;
        BannerScope scope = new BannerScope();
        scope.setScopeCode(scopeType);
        scope.setScopeId(scopeId);
        
        // 不设置场景，以测试老版本没有场景时是否兼容
        List<String> sceneTypeList = new ArrayList<>();
        sceneTypeList.add("pm_admin");
        
        UpdateBannerByOwnerCommand cmd = new UpdateBannerByOwnerCommand();
        cmd.setName(name);
        cmd.setPosterPath(posterUri);
        cmd.setActionType(actionType);
        cmd.setActionData(actionData);
        cmd.setStatus(status);
        cmd.setActionType(actionType);
        cmd.setActionData(actionData);
        cmd.setDefaultOrder(order);
        cmd.setOwnerId(1000750L);
        cmd.setOwnerType("organization");
        cmd.setId(result.get(0).getId());
        cmd.setScope(scope);
        
        String commandRelativeUri = "/banner/updateBannerByOwner";
        RestResponse resp = httpClientService.restPost(commandRelativeUri, cmd, RestResponse.class, this.context);
        
        assertNotNull("The reponse of may not be null", resp);
        assertTrue("The banner should be created, response=" + StringHelper.toJsonString(resp), httpClientService.isReponseSuccess(resp));
        
        List<EhBanners> ehBanners = context.select().from(EH_BANNERS)
        		.where(EH_BANNERS.NAME.eq(name))
        		.and(EH_BANNERS.ACTION_DATA.eq(actionData))
        		.and(EH_BANNERS.SCOPE_ID.eq(scopeId))
	            .fetch().map((r) -> {
	                result.add(ConvertHelper.convert(r, EhBanners.class));
	                return null;
	            });
        assertEquals(1, ehBanners.size());
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

