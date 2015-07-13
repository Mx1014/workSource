// @formatter:off
package com.everhomes.launchpad;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.elasticsearch.cluster.routing.RotationShardShuffler;
import org.json.simple.JSONArray;
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

import com.everhomes.junit.PropertyInitializer;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;

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
        
        cmd.setItemLocation("/home");
        cmd.setItemGroup(ItemGroup.GOVAGENCIES.getCode());
        cmd.setItemLabel("物业");
        cmd.setItemName("xx");
        cmd.setActionType(ActionType.BIZ_DETAILS.getCode());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemLocation", "/home/Pm/");
        cmd.setActionData(jsonObject.toString());
        cmd.setDisplayFlag((byte)1);
        
        
        List<ItemScope> itemScopes = new ArrayList<ItemScope>();
        ItemScope scope1 = new ItemScope();
        scope1.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        scope1.setDefaultOrder(0);
        scope1.setScopeId(5636106L);
        scope1.setScopeType(LaunchPadScopeType.CITY.getCode());
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
    public void getItemList(){
        User user = new User();
        user.setId(10001L);
        UserContext.current().setUser(user);
        UserLogin login = new UserLogin();
        login.setUserId(100001);
        UserContext.current().setLogin(login);
        
        UserContext.current().getLogin().getLoginToken().getTokenString();
        GetLaunchPadItemsCommand cmd = new GetLaunchPadItemsCommand();
        cmd.setCommunityId(8L);
        cmd.setItemGroup(ItemGroup.CALLPHONES.getCode());
        cmd.setItemLocation("/home/Pm");
        GetLaunchPadItemsCommandResponse response = launchPadService.getLaunchPadItems(cmd);
        for(LaunchPadItemDTO dto : response.getLaunchPadItems()){
            System.out.println(dto);
        }
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
    
    @Test
    public void createLaunchPadLayout(){
        CreateLaunchPadLayoutCommand cmd = new CreateLaunchPadLayoutCommand();
        cmd.setMinVersionCode(3L);
        cmd.setName("v3.0");
        cmd.setNamespaceId(0);
        cmd.setStatus((byte)2);
        cmd.setVersionCode(3L);
       
        cmd.setLayoutJson(createLayouts().toJSONString());
        this.launchPadService.createLaunchPadLayout(cmd);
    }
    @SuppressWarnings("unchecked")
    private JSONObject createLayouts() {
        JSONObject root = new JSONObject();                                                                                                             
        root.put("versionCode", "2015061701");                                                        
        root.put("versionName", "3.0.0");                                                         
        JSONArray layouts = new JSONArray();                                                                                                            

        // 示例layout和group                                                                                                                           
        JSONArray exampleGroups = new JSONArray();                                                                                                      
        JSONObject exampleBannerGroup = new JSONObject();                                                                                               
        exampleBannerGroup.put("groupName", "String, 说明：分组显示名称，用于显示，若显示时不需要名称则留空");                                          
        exampleBannerGroup.put("widget", "String, 说明：组内控件，如Navigator、Banners、Coupons、Posts");                                               
        exampleBannerGroup.put("instanceConfig", "String, json格式，说明：widget实例相关的配置，不需要时为空，如Default、GovAgencies、Bizs、GaActions");
          exampleBannerGroup.put("style", "String, 说明：组内控件风格，如Default、Metro、Light");                                                               
          exampleBannerGroup.put("defaultOrder", "Integer, 组排列顺序");                                                                                
          exampleBannerGroup.put("separatorFlag", "Byte, 说明：组底部是否有分隔条，0: no, 1: yes");                                                     
          exampleBannerGroup.put("separatorHeight", "Integer, 说明：组底部分隔条高度,单位px");                                                                  
          exampleGroups.add(exampleBannerGroup);                                                                                                        

          JSONObject exampleLayout = new JSONObject();                                                                                                  
          exampleLayout.put("layoutName", "String, 说明：单个layout的名称，用于识别是哪个layout，如ServiceMarket");                                     
          exampleLayout.put("groups", exampleGroups);
          exampleLayout.put("displayName", "当前layout的标题");
          //layouts.add(exampleLayout);                                                                                                                   



          // 服务市场首页layout和group                                                                                                                  
          JSONArray marketGroups = new JSONArray();                                                                                                     
          JSONObject mktBannerGroup = new JSONObject();                                                                                                 
          mktBannerGroup.put("groupName", "");                                                                                                          
          mktBannerGroup.put("widget", "Banners");                                                                                                      
          JSONObject mktBannerConfig = new JSONObject();                                                                                                
          mktBannerConfig.put("itemGroup", "Default");                                                                                                  
          mktBannerGroup.put("instanceConfig", mktBannerConfig);                                                                                        
          mktBannerGroup.put("style", "Default");                                                                                                       
          mktBannerGroup.put("defaultOrder", 1);                                                                                                      
          mktBannerGroup.put("separatorFlag", 0); // no separator                                                                                     
          mktBannerGroup.put("separatorHeight", 0);                                                                                                    
          marketGroups.add(mktBannerGroup);                                                                                                             

          JSONObject gaGroup = new JSONObject();                                                                                                        
          gaGroup.put("groupName", "");                                                                                                                 
          gaGroup.put("widget", "Navigator");                                                                                                           
          JSONObject gaConfig = new JSONObject();                                                                                                       
          gaConfig.put("itemGroup", "GovAgencies");                                                                                                     
          gaGroup.put("instanceConfig", gaConfig);                                                                                                      
          gaGroup.put("style", "Light");                                                                                                              
          gaGroup.put("defaultOrder", 2);                                                                                                             
          gaGroup.put("separatorFlag", 1); // has separator                                                                                           
          gaGroup.put("separatorHeight", 21);                                                                                                        
          marketGroups.add(gaGroup);                                                                                                                    

          JSONObject couponGroup = new JSONObject();                                                                                                    
          couponGroup.put("groupName", "");                                                                                                             
          couponGroup.put("widget", "Coupons");                                                                                                         
          JSONObject couponConfig = new JSONObject();                                                                                                   
          couponConfig.put("itemGroup", "Default");                                                                                                     
          couponGroup.put("instanceConfig", couponConfig);                                                                                              
          couponGroup.put("style", "Default");                                                                                                          
          couponGroup.put("defaultOrder", 3);                                                                                                         
          couponGroup.put("separatorFlag", 1); // has separator                                                                                       
          couponGroup.put("separatorHeight", 21);                                                                                                    
          marketGroups.add(couponGroup); 

          JSONObject actionGroup = new JSONObject();                                                                                                  
          actionGroup.put("groupName", "");                                                                                                           
          actionGroup.put("widget", "Navigator");                                                                                                     
          JSONObject actionConfig = new JSONObject();                                                                                                 
          actionConfig.put("itemGroup", "GaActions");                                                                                                 
          actionGroup.put("instanceConfig", actionConfig);                                                                                          
          actionGroup.put("style", "Metro");                                                                                                           
          actionGroup.put("defaultOrder", 4);                                                                                                       
          actionGroup.put("separatorFlag", 1); // has separator                                                                                     
          actionGroup.put("separatorHeight", 21);                                                                                                  
          marketGroups.add(actionGroup);      
          
          JSONObject bizGroup = new JSONObject();                                                                                                       
          bizGroup.put("groupName", "热销商品");                                                                                                        
          bizGroup.put("widget", "Navigator");                                                                                                          
          JSONObject bizConfig = new JSONObject();                                                                                                      
          bizConfig.put("itemGroup", "Bizs");                                                                                                           
          bizGroup.put("instanceConfig", bizConfig);                                                                                                    
          bizGroup.put("style", "Default");                                                                                                             
          bizGroup.put("defaultOrder", 5);                                                                                                            
          bizGroup.put("separatorFlag", 0); // no separator                                                                                           
          bizGroup.put("separatorHeight", 0);                                                                                                       
          marketGroups.add(bizGroup);
          
           


//          JSONObject marketLayout = new JSONObject();                                                                                                   
//          marketLayout.put("layoutName", "ServiceMarketLayout");                                                                                        
//          marketLayout.put("groups", marketGroups);    
//          marketLayout.put("displayName", "服务市场");                                                                                               
//          root.put("layoutName", "ServiceMarketLayout");                                                                                        
//          root.put("groups", marketGroups);    
//          root.put("displayName", "服务市场");
          
          //layouts.add(marketLayout);                                                                                                                    



          // 物业相关layout和group                                                                                                                      
          JSONArray pmGroups = new JSONArray();                                                                                                         
//          JSONObject pmBannerGroup = new JSONObject();                                                                                                  
//          pmBannerGroup.put("groupName", "");                                                                                                           
//          pmBannerGroup.put("widget", "Banners");                                                                                                       
//          JSONObject pmConfig = new JSONObject();                                                                                                       
//          pmConfig.put("itemGroup", "Default");                                                                                                         
//          pmBannerGroup.put("instanceConfig", pmConfig);                                                                                                
//          pmBannerGroup.put("style", "Default");                                                                                                        
//          pmBannerGroup.put("defaultOrder", 1);                                                                                                       
//          pmBannerGroup.put("separatorFlag", 0); // no separator                                                                                      
//          pmBannerGroup.put("separatorHeight", 0);                                                                                                     
//          pmGroups.add(pmBannerGroup);
          
          JSONObject pmActionBarGroup = new JSONObject();                                                                                                  
          pmActionBarGroup.put("groupName", "actionBar");                                                                                                           
          pmActionBarGroup.put("widget", "ActionBar");                                                                                                       
          JSONObject pmConfig = new JSONObject();                                                                                                       
          pmConfig.put("itemGroup", "ActionBar");                                                                                                         
          pmActionBarGroup.put("instanceConfig", pmConfig);                                                                                                
          pmActionBarGroup.put("style", "Default");                                                                                                        
          pmActionBarGroup.put("defaultOrder", 1);                                                                                                       
          pmActionBarGroup.put("separatorFlag", 1); // no separator                                                                                      
          pmActionBarGroup.put("separatorHeight", 21);                                                                                                     
          pmGroups.add(pmActionBarGroup);    

          JSONObject pmActionGroup = new JSONObject();                                                                                                  
          pmActionGroup.put("groupName", "");                                                                                                           
          pmActionGroup.put("widget", "Navigator");                                                                                                     
          JSONObject pmActionConfig = new JSONObject();                                                                                                 
          pmActionConfig.put("itemGroup", "GaActions");                                                                                                 
          pmActionGroup.put("instanceConfig", pmActionConfig);                                                                                          
          pmActionGroup.put("style", "Metro");                                                                                                           
          pmActionGroup.put("defaultOrder", 2);                                                                                                       
          pmActionGroup.put("separatorFlag", 1); // has separator                                                                                     
          pmActionGroup.put("separatorHeight", 21);                                                                                                  
          pmGroups.add(pmActionGroup);                                                                                                                  

          JSONObject gaPostGroup = new JSONObject();                                                                                                    
          gaPostGroup.put("groupName", "");                                                                                                             
          gaPostGroup.put("widget", "Posts");                                                                                                           
          JSONObject pmPostConfig = new JSONObject();                                                                                                   
          pmPostConfig.put("itemGroup", "GaActions");                                                                                                     
          gaPostGroup.put("instanceConfig", pmPostConfig);                                                                                              
          gaPostGroup.put("style", "Default");                                                                                                          
          gaPostGroup.put("defaultOrder", 3);                                                                                                         
          gaPostGroup.put("separatorFlag", 1); // has separator                                                                                       
          gaPostGroup.put("separatorHeight", 21);                                                                                                    
          pmGroups.add(gaPostGroup);
          
          JSONObject callPhoneGroup = new JSONObject();                                                                                                    
          gaPostGroup.put("groupName", "CallPhone");                                                                                                             
          gaPostGroup.put("widget", "CallPhones");                                                                                                           
          JSONObject pmCallPhoneConfig = new JSONObject();                                                                                                   
          pmPostConfig.put("itemGroup", "CallPhones");                                                                                                     
          gaPostGroup.put("instanceConfig", pmCallPhoneConfig);                                                                                              
          gaPostGroup.put("style", "Default");                                                                                                          
          gaPostGroup.put("defaultOrder", 3);                                                                                                         
          gaPostGroup.put("separatorFlag", 0); // has separator                                                                                       
          gaPostGroup.put("separatorHeight", 0);                                                                                                    
          pmGroups.add(callPhoneGroup);
          

//          JSONObject gaLayout = new JSONObject();                                                                                                       
//          gaLayout.put("layoutName", "PmLayout");                                                                                                       
//          gaLayout.put("groups", pmGroups);
//          gaLayout.put("displayName", "物业首页");
//          layouts.add(gaLayout);                                                                                                     
          root.put("layoutName", "PmLayout");                                                                                                       
          root.put("groups", pmGroups);
          root.put("displayName", "物业首页");        

          //root.put("layouts", marketLayout);
          System.out.println(root.toString());
          return root;
    }
    @Test
    public void findLaunchPadLayout(){
        GetLaunchPadLayoutByVersionCodeCommand cmd = new GetLaunchPadLayoutByVersionCodeCommand();
        cmd.setVersionCode(3L);
        System.out.println(this.launchPadService.getLastLaunchPadLayoutByVersionCode(cmd));
    }
   
}
