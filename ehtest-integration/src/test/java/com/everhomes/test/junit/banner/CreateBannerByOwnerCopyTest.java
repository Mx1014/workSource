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
import com.everhomes.rest.banner.CreateBannerByOwnerCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhBanners;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class CreateBannerByOwnerCopyTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    /**
     * 测试创建banner时创建动作
     */
    @Test
    public void testCreateBannerByOwner_testCreate() {
    	Integer namespaceId = 2;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String name = "new add banner";
        String location = "/home";
        String group = "DEFAULT";
        String posterUri = "cs://1/image/aW1hZ2UvTVRvM1ltRm1ORE01WVRNeU56UmtZakZqWVRRME1tVmhabUpqT0RNMU1qazRNUQ";
        byte actionType = 14;
        String actionData = "{\"url\":\"www.zuolin.com\"}"; 
        int order = 10;
        byte status = BannerStatus.ACTIVE.getCode();

        byte scopeType = ScopeType.COMMUNITY.getCode();
        long scopeId = 24210090697426103L;
        BannerScope scope = new BannerScope();
        scope.setScopeCode(scopeType);
        scope.setScopeId(scopeId);
        scope.setOrder(order);
        List<String> sceneTypeList = new ArrayList<>();
        sceneTypeList.add("pm_admin");
        sceneTypeList.add("default");
        
        CreateBannerByOwnerCommand cmd = new CreateBannerByOwnerCommand();
        cmd.setOwnerType("organization");
        cmd.setOwnerId(1000750L);
        cmd.setName(name);
        cmd.setBannerLocation(location);
        cmd.setBannerGroup(group);
        cmd.setPosterPath(posterUri);
        cmd.setActionType(actionType);
        cmd.setActionData(actionData);
        cmd.setStatus(status);
        cmd.setScope(scope);
        cmd.setSceneTypes(sceneTypeList);
        cmd.setDefaultOrder(order);
        
        String commandRelativeUri = "/banner/createBannerByOwner";
        RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The banner should be created, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        List<EhBanners> result = context.select().from(EH_BANNERS)
	        .where(EH_BANNERS.NAME.eq(name))
	        .and(EH_BANNERS.STATUS.eq(status))
	        .and(EH_BANNERS.SCOPE_CODE.eq(scopeType))
	        .and(EH_BANNERS.SCOPE_ID.eq(scopeId))
	        .and(EH_BANNERS.ACTION_TYPE.eq(actionType))
	        .and(EH_BANNERS.ACTION_DATA.eq(actionData))
	        .and(EH_BANNERS.ORDER.eq(order))
	        .and(EH_BANNERS.APPLY_POLICY.eq(Byte.parseByte("3")))
            .fetch().into(EhBanners.class);
        
        assertEquals(2, result.size());
    }
    
    /**
     * 测试创建banner时复制的动作
     */
    @Test
    public void testCreateBannerByOwner_testCopy() {
    	Integer namespaceId = 2;
    	String userIdentifier = "12000000001";
    	String plainTexPassword = "123456";
    	
    	logon(namespaceId, userIdentifier, plainTexPassword);
    	
    	String name = "new add banner";
    	String location = "/home";
    	String group = "DEFAULT";
    	String posterUri = "cs://1/image/aW1hZ2UvTVRvM1ltRm1ORE01WVRNeU56UmtZakZqWVRRME1tVmhabUpqT0RNMU1qazRNUQ";
    	byte actionType = 14;
    	String actionData = "{\"url\":\"www.zuolin.com\"}"; 
    	int order = 10;
    	byte status = BannerStatus.ACTIVE.getCode();
    	
    	byte scopeType = ScopeType.COMMUNITY.getCode();
    	long scopeId = 24210090697426103L;
    	BannerScope scope = new BannerScope();
    	scope.setScopeCode(scopeType);
    	scope.setScopeId(scopeId);
    	scope.setOrder(order);
    	List<String> sceneTypeList = new ArrayList<>();
    	sceneTypeList.add("pm_admin");
    	sceneTypeList.add("default");
    	
    	CreateBannerByOwnerCommand cmd = new CreateBannerByOwnerCommand();
    	cmd.setOwnerType("organization");
    	cmd.setOwnerId(1000750L);
    	cmd.setName(name);
    	cmd.setBannerLocation(location);
    	cmd.setBannerGroup(group);
    	cmd.setPosterPath(posterUri);
    	cmd.setActionType(actionType);
    	cmd.setActionData(actionData);
    	cmd.setStatus(status);
    	cmd.setScope(scope);
    	cmd.setSceneTypes(sceneTypeList);
    	cmd.setDefaultOrder(order);
    	
    	String commandRelativeUri = "/banner/createBannerByOwner";
    	RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);
    	
    	assertNotNull("The reponse of may not be null", response);
    	assertTrue("The banner should be created, response=" + 
    			StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    	
    	DSLContext context = dbProvider.getDslContext();
    	Integer count = context.selectCount().from(Tables.EH_BANNERS).where(Tables.EH_BANNERS.NAME.ne(name)).fetchOne().into(Integer.class);
        assertEquals(Integer.valueOf(6), count);
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
		String userInfoFilePath = "data/json/create-banner-by-owner-create-test-data.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

