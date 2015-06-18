// @formatter:off
package com.everhomes.launchpad;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.json.simple.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.app.AppConstants;
import com.everhomes.category.CategoryType;
import com.everhomes.community.CommunityProvider;
import com.everhomes.junit.PropertyInitializer;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.google.gson.JsonObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class LaunchPadTest extends TestCase {
    
    @Autowired
    private LaunchPadService launchPadService;
    
    
    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
            FreeMarkerAutoConfiguration.class
        })
    static class ContextConfiguration {
    }
    
    //@Before
    public void setup() {
        
    }
    
    //@After
    public void teardown() {
       
    }
    
    @Test
    public void testCreateLaunchPadItems() {
        CreateLaunchPadItemCommand cmd = new  CreateLaunchPadItemCommand();
        
        cmd.setItemTag(ItemTag.SYS_BIZS.getCode());
        cmd.setItemLabel("百度");
        cmd.setItemName(ItemNameTag.BIZ.getCode());
        cmd.setActionType(ActionType.NONE.getCode());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "www.baidu.com");
        cmd.setActionData(jsonObject.toString());
        cmd.setDisplayFlag((byte)1);
        
        
        List<ItemScope> itemScopes = new ArrayList<ItemScope>();
        ItemScope scope1 = new ItemScope();
        scope1.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
        scope1.setDefaultOrder(0);
        scope1.setScopeId(0L);
        scope1.setScopeType(LaunchPadScopeType.COUNTRY.getCode());
        itemScopes.add(scope1);
        
//        ItemScope scope2 = new ItemScope();
//        scope2.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
//        scope2.setDefaultOrder(0);
//        scope2.setScopeId(0L);
//        scope2.setScopeType(LaunchPadScopeType.COUNTRY.getCode());
//        itemScopes.add(scope1);
        cmd.setItemScopes(itemScopes);
        launchPadService.createLaunchPadItem(cmd);
    }
    @Test
    public void userDefinedLaunchPad(){
        User user = new User();
        user.setId(10001L);
        UserContext.current().setUser(user);
        UserDefinedLaunchPadCommand cmd = new UserDefinedLaunchPadCommand();
        Item item1  = new Item();
        item1.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item1.setId(4L);
        item1.setOrderIndex(0);
        
//        Item item2  = new Item();
//        item2.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
//        item2.setId(2L);
//        item2.setOrderIndex(0);
        
        Item item3  = new Item();
        item3.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item3.setId(5L);
        item3.setOrderIndex(0);
        item3.setDisplayFlag((byte)0);
        
//        Item item4  = new Item();
//        item4.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
//        item4.setId(6L);
//        item4.setOrderIndex(0);
        
        Item item5  = new Item();
        item5.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item5.setId(7L);
        item5.setOrderIndex(0);
        
        Item item6  = new Item();
        item6.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item6.setId(8L);
        item6.setOrderIndex(0);
        
        
        List<Item> items = new ArrayList<Item>();
        items.add(item1);
        items.add(item3);
//        items.add(item5);
//        items.add(item6);
        cmd.setItems(items);
        
        
        this.launchPadService.userDefinedLaunchPad(cmd);
    }
    
    @Ignore @Test
    public void deleteLaunchPadItems(){
        DeleteLaunchPadItemCommand cmd = new DeleteLaunchPadItemCommand();
        List<Long> itemIds = new ArrayList<Long>();
        itemIds.add(10L);
        cmd.setItemIds(itemIds);
        this.launchPadService.deleteLaunchPadItem(cmd);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void createLaunchPadLayout(){
        CreateLaunchPadLayoutCommand cmd = new CreateLaunchPadLayoutCommand();
        cmd.setMinVersionCode(3L);
        cmd.setName("v3.0");
        cmd.setNamespaceId(0);
        cmd.setStatus((byte)2);
        cmd.setVersionCode(3L);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "group1");
        jsonObject.put("type", "PM");
        jsonObject.put("style", "Nav_Win8");
        cmd.setLayoutJson(jsonObject.toJSONString());
        this.launchPadService.createLaunchPadLayout(cmd);
    }
    @Test
    public void findLaunchPadLayout(){
        GetLaunchPadLayoutByVersionCodeCommand cmd = new GetLaunchPadLayoutByVersionCodeCommand();
        cmd.setVersionCode(3L);
        System.out.println(this.launchPadService.getLastLaunchPadLayoutByVersionCode(cmd));
    }
   
}
