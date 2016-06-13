// @formatter:off
package com.everhomes.test.junit.banner;

import static com.everhomes.server.schema.Tables.EH_BANNERS;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.banner.admin.CreateBannerAdminCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.scene.admin.SceneListSceneTypesRestResponse;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.server.schema.tables.pojos.EhBanners;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class CreateBannerByAdminTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    /**
     * 老版本没有指定场景，应该也能够正确创建默认场景的banner
     */
    @Test
    public void createBannerWithDefaultScenes() {
        Integer namespaceId = 0;
        String userIdentifier = "12900000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        String name = "你秀你的恩爱，我买我的期待";
        String location = "/home";
        String group = "DEFAULT";
        String posterUri = "cs://1/image/aW1hZ2UvTVRvM1ltRm1ORE01WVRNeU56UmtZakZqWVRRME1tVmhabUpqT0RNMU1qazRNUQ";
        byte actionType = 9;
        String actionData = "{\"forumId\":100334,\"topicId\":175219}"; 
        int order = 10;
        byte status = BannerStatus.ACTIVE.getCode();

        byte scopeType = ScopeType.ALL.getCode();
        long scopeId = 0L;
        BannerScope scope = new BannerScope();
        scope.setScopeCode(scopeType);
        scope.setScopeId(scopeId);
        scope.setOrder(order);
        List<BannerScope> scopeList = new ArrayList<BannerScope>();
        scopeList.add(scope);
        
        // 不设置场景，以测试老版本没有场景时是否兼容
        List<String> sceneTypeList = null;
        
        CreateBannerAdminCommand cmd = new CreateBannerAdminCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setName(name);
        cmd.setBannerLocation(location);
        cmd.setBannerGroup(group);
        cmd.setPosterPath(posterUri);
        cmd.setActionType(actionType);
        cmd.setActionData(actionData);
        //cmd.setOrder(order); // 注意：order是使用scope中的order，而不是cmd里的order
        cmd.setStatus(status);
        cmd.setScopes(scopeList);
        cmd.setSceneTypeList(sceneTypeList);
        
        String commandRelativeUri = "/admin/banner/createBanner";
        SceneListSceneTypesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
            SceneListSceneTypesRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The banner should be created, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhBanners> result = new ArrayList<EhBanners>();
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_BANNERS)
            .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, EhBanners.class));
                return null;
            });
        // 没设置场景时应该只有一条记录
        assertEquals(1, result.size());
        
        EhBanners banner = result.get(0);
        assertEquals(name, banner.getName());
        assertEquals(location, banner.getBannerLocation());
        assertEquals(group, banner.getBannerGroup());
        assertEquals(posterUri, banner.getPosterPath());
        assertEquals(Byte.valueOf(actionType), banner.getActionType());
        assertEquals(actionData, banner.getActionData());
        assertEquals(Integer.valueOf(order), banner.getOrder());
        assertEquals(Byte.valueOf(status), banner.getStatus());
        assertEquals(Byte.valueOf(scopeType), banner.getScopeCode());
        assertEquals(Long.valueOf(scopeId), banner.getScopeId());
        
        SceneType sceneType = SceneType.fromCode(banner.getSceneType());
        assertEquals(SceneType.DEFAULT, sceneType);
    }
    
    @Test
    public void createBannerWithMultiScenes() {
        Integer namespaceId = 0;
        String userIdentifier = "12900000001"; // 管理员帐号
        String plainTexPassword = "123456";
        logon(null, userIdentifier, plainTexPassword);
        
        String name = "你秀你的恩爱，我买我的期待";
        String location = "/home";
        String group = "DEFAULT";
        String posterUri = "cs://1/image/aW1hZ2UvTVRvM1ltRm1ORE01WVRNeU56UmtZakZqWVRRME1tVmhabUpqT0RNMU1qazRNUQ";
        byte actionType = 9;
        String actionData = "{\"forumId\":100334,\"topicId\":175219}"; 
        int order = 10;
        byte status = BannerStatus.ACTIVE.getCode();

        byte scopeType = ScopeType.ALL.getCode();
        long scopeId = 0L;
        BannerScope scope = new BannerScope();
        scope.setScopeCode(scopeType);
        scope.setScopeId(scopeId);
        scope.setOrder(order);
        List<BannerScope> scopeList = new ArrayList<BannerScope>();
        scopeList.add(scope);
        
        List<String> sceneTypeList = new ArrayList<String>();
        sceneTypeList.add(SceneType.DEFAULT.getCode());
        sceneTypeList.add(SceneType.PM_ADMIN.getCode());
        
        CreateBannerAdminCommand cmd = new CreateBannerAdminCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setName(name);
        cmd.setBannerLocation(location);
        cmd.setBannerGroup(group);
        cmd.setPosterPath(posterUri);
        cmd.setActionType(actionType);
        cmd.setActionData(actionData);
        //cmd.setOrder(order); // 注意：order是使用scope中的order，而不是cmd里的order
        cmd.setStatus(status);
        cmd.setScopes(scopeList);
        cmd.setSceneTypeList(sceneTypeList);
        
        String commandRelativeUri = "/admin/banner/createBanner";
        SceneListSceneTypesRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
            SceneListSceneTypesRestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The banner should be created, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        List<EhBanners> result = new ArrayList<EhBanners>();
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_BANNERS)
            .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, EhBanners.class));
                return null;
            });
        // 两个场景应该有两条记录
        assertEquals(2, result.size());
        boolean defaultSceneExist = false;
        boolean pmadminSceneExist = false;
        for(EhBanners banner : result) {
            SceneType sceneType = SceneType.fromCode(banner.getSceneType());
            if(SceneType.DEFAULT == sceneType) {
                defaultSceneExist = true;
            }
            if(SceneType.PM_ADMIN == sceneType) {
                pmadminSceneExist = true;
            }
            
            assertEquals(name, banner.getName());
            assertEquals(location, banner.getBannerLocation());
            assertEquals(group, banner.getBannerGroup());
            assertEquals(posterUri, banner.getPosterPath());
            assertEquals(Byte.valueOf(actionType), banner.getActionType());
            assertEquals(actionData, banner.getActionData());
            assertEquals(Integer.valueOf(order), banner.getOrder());
            assertEquals(Byte.valueOf(status), banner.getStatus());
            assertEquals(Byte.valueOf(scopeType), banner.getScopeCode());
            assertEquals(Long.valueOf(scopeId), banner.getScopeId());
        }
        assertTrue("there should be a banner in default scene", defaultSceneExist);
        assertTrue("there should be a banner in pm_admin scene", pmadminSceneExist);
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.4.x-test-data-scene_types_160607.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
        jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}

