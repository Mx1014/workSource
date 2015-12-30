// @formatter:off
package com.everhomes.launchpad;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.core.util.ContextUtil;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.launchpad.ApplyPolicy;
import com.everhomes.rest.launchpad.GetLaunchPadItemsCommand;
import com.everhomes.rest.launchpad.GetLaunchPadItemsCommandResponse;
import com.everhomes.rest.launchpad.GetLaunchPadLayoutByVersionCodeCommand;
import com.everhomes.rest.launchpad.Item;
import com.everhomes.rest.launchpad.ItemGroup;
import com.everhomes.rest.launchpad.ItemScope;
import com.everhomes.rest.launchpad.UserDefinedLaunchPadCommand;
import com.everhomes.rest.launchpad.admin.CreateLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.CreateLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.DeleteLaunchPadItemAdminCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserFavorite;
import com.everhomes.user.UserLogin;
import com.everhomes.util.DateHelper;
import com.everhomes.util.WebTokenGenerator;

public class LaunchPadTest extends CoreServerTestCase {
    
    @Autowired
    private LaunchPadService launchPadService;
    @Autowired
    private UserActivityProvider userActivityProvider;
    
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

    
    @Before
    public void setup() {
//        User user = new User();
//        user.setId(10001L);
//        UserContext.current().setUser(user);
//        UserLogin login = new UserLogin();
//        login.setUserId(100001);
//        UserContext.current().setLogin(login);
    }
    
    //@After
    public void teardown() {
       
    }
    
    @Test
    public void testCreateLaunchPadItems() {
        CreateLaunchPadItemAdminCommand cmd = new  CreateLaunchPadItemAdminCommand();
        
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
        scope1.setScopeCode(ScopeType.CITY.getCode());
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
        
        //WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        GetLaunchPadItemsCommand cmd = new GetLaunchPadItemsCommand();
        cmd.setCommunityId(8L);
        cmd.setItemGroup(ItemGroup.BIZS.getCode());
        cmd.setItemLocation("/home");
        GetLaunchPadItemsCommandResponse response = this.launchPadService.getLaunchPadItems(cmd, null);
        
        System.out.println(response);
    }
    @Test
    public void userDefinedLaunchPad(){
        User user = new User();
        user.setId(10001L);
        UserContext.current().setUser(user);
        UserDefinedLaunchPadCommand cmd = new UserDefinedLaunchPadCommand();
        Item item1  = new Item();
        item1.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item1.setId(24L);
        item1.setOrderIndex(0);
        item1.setDisplayFlag((byte)0);
        
//        Item item2  = new Item();
//        item2.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
//        item2.setId(2L);
//        item2.setOrderIndex(0);
        
        Item item3  = new Item();
        item3.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item3.setId(25L);
        item3.setOrderIndex(0);
        item3.setDisplayFlag((byte)0);
        item3.setDisplayFlag((byte)0);
        
//        Item item4  = new Item();
//        item4.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
//        item4.setId(6L);
//        item4.setOrderIndex(0);
        
        Item item5  = new Item();
        item5.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item5.setId(26L);
        item5.setOrderIndex(0);
        item5.setDisplayFlag((byte)0);
        
        Item item6  = new Item();
        item6.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item6.setId(55L);
        item6.setOrderIndex(0);
        item6.setDisplayFlag((byte)0);
        
        
        List<Item> items = new ArrayList<Item>();
        items.add(item1);
        items.add(item3);
        items.add(item5);
        items.add(item6);
        cmd.setItems(items);
        
        
        this.launchPadService.userDefinedLaunchPad(cmd);
    }
    
    @Ignore @Test
    public void deleteLaunchPadItems(){
        DeleteLaunchPadItemAdminCommand cmd = new DeleteLaunchPadItemAdminCommand();
        List<Long> itemIds = new ArrayList<Long>();
        itemIds.add(10L);
        cmd.setItemIds(itemIds);
        this.launchPadService.deleteLaunchPadItem(cmd);
    }
    
    @Test
    public void createLaunchPadLayout(){
        CreateLaunchPadLayoutAdminCommand cmd = new CreateLaunchPadLayoutAdminCommand();
        cmd.setMinVersionCode(3L);
        cmd.setName("v3.0");
        cmd.setNamespaceId(0);
        cmd.setStatus((byte)2);
        cmd.setVersionCode(3L);
       
        cmd.setLayoutJson(createLayouts().toJSONString());
        this.launchPadService.createLaunchPadLayout(cmd);
    }
    @Test
    public void addUserFavorite(){
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setTargetId(1l);
        userFavorite.setTargetType("biz");
        userFavorite.setOwnerUid(1l);
        userFavorite.setId(1l);
        userFavorite.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        userActivityProvider.addUserFavorite(userFavorite);
    }
    
    @SuppressWarnings("unchecked")
    private JSONObject createLayouts() {
        JSONObject root = new JSONObject();                                                                                                             
        root.put("versionCode", "2015072815");                                                        
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
          exampleBannerGroup.put("columnCount", "一行显示图标数量");
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
          gaGroup.put("style", "Default");                                                                                                              
          gaGroup.put("defaultOrder", 2);                                                                                                             
          gaGroup.put("separatorFlag", 1); // has separator                                                                                           
          gaGroup.put("separatorHeight", 21);
          gaGroup.put("columnCount", 4);
          marketGroups.add(gaGroup);                                                                                                                    

          JSONObject couponGroup = new JSONObject();                                                                                                    
          couponGroup.put("groupName", "");                                                                                                             
          couponGroup.put("widget", "Coupons");                                                                                                         
          JSONObject couponConfig = new JSONObject();                                                                                                   
          couponConfig.put("itemGroup", "coupons");                                                                                                     
          couponGroup.put("instanceConfig", couponConfig);                                                                                              
          couponGroup.put("style", "Default");                                                                                                          
          couponGroup.put("defaultOrder", 3);                                                                                                         
          couponGroup.put("separatorFlag", 1); // has separator                                                                                       
          couponGroup.put("separatorHeight", 21);                                                                                                    
          marketGroups.add(couponGroup); 
          
          JSONObject defalultGroup = new JSONObject();                                                                                                  
          defalultGroup.put("groupName", "");                                                                                                           
          defalultGroup.put("widget", "Navigator");                                                                                                     
          JSONObject actionConfig = new JSONObject();                                                                                                 
          actionConfig.put("itemGroup", "Default");                                                                                                 
          defalultGroup.put("instanceConfig", actionConfig);                                                                                          
          defalultGroup.put("style", "Default");                                                                                                           
          defalultGroup.put("defaultOrder", 4);                                                                                                       
          defalultGroup.put("separatorFlag", 1); // has separator                                                                                     
          defalultGroup.put("separatorHeight", 21); 
          defalultGroup.put("columnCount", 4);
          marketGroups.add(defalultGroup);

//          JSONObject actionGroup = new JSONObject();                                                                                                  
//          actionGroup.put("groupName", "");                                                                                                           
//          actionGroup.put("widget", "Navigator");                                                                                                     
//          JSONObject actionConfig = new JSONObject();                                                                                                 
//          actionConfig.put("itemGroup", "GaActions");                                                                                                 
//          actionGroup.put("instanceConfig", actionConfig);                                                                                          
//          actionGroup.put("style", "Metro");                                                                                                           
//          actionGroup.put("defaultOrder", 4);                                                                                                       
//          actionGroup.put("separatorFlag", 1); // has separator                                                                                     
//          actionGroup.put("separatorHeight", 21);                                                                                                  
//          marketGroups.add(actionGroup);      
          
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
          
           

                                                                                             
          root.put("layoutName", "ServiceMarketLayout");  
          root.put("displayName", "服务市场");
          root.put("groups", marketGroups);    
          
          
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
          
//          JSONObject pmActionBarGroup = new JSONObject();                                                                                                  
//          pmActionBarGroup.put("groupName", "actionBar");                                                                                                           
//          pmActionBarGroup.put("widget", "ActionBars");                                                                                                       
//          JSONObject pmConfig = new JSONObject();                                                                                                       
//          pmConfig.put("itemGroup", "ActionBars");                                                                                                         
//          pmActionBarGroup.put("instanceConfig", pmConfig);                                                                                                
//          pmActionBarGroup.put("style", "Default");                                                                                                        
//          pmActionBarGroup.put("defaultOrder", 1);                                                                                                       
//          pmActionBarGroup.put("separatorFlag", 0); // no separator                                                                                      
//          pmActionBarGroup.put("separatorHeight", 0);                                                                                                     
//          pmGroups.add(pmActionBarGroup);    

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
          pmActionGroup.put("columnCount", 4);
          pmGroups.add(pmActionGroup);                                                                                                                  

          JSONObject gaPostGroup = new JSONObject();                                                                                                    
          gaPostGroup.put("groupName", "");                                                                                                             
          gaPostGroup.put("widget", "Posts");                                                                                                           
          JSONObject pmPostConfig = new JSONObject();                                                                                                   
          pmPostConfig.put("itemGroup", "GaPosts");                                                                                                     
          gaPostGroup.put("instanceConfig", pmPostConfig);                                                                                              
          gaPostGroup.put("style", "Default");                                                                                                          
          gaPostGroup.put("defaultOrder", 3);                                                                                                         
          gaPostGroup.put("separatorFlag", 0); // has separator                                                                                       
          gaPostGroup.put("separatorHeight", 0);                                                                                                    
          pmGroups.add(gaPostGroup);
          
          JSONObject callPhoneGroup = new JSONObject();                                                                                                    
          callPhoneGroup.put("groupName", "CallPhone");                                                                                                             
          callPhoneGroup.put("widget", "CallPhones");                                                                                                           
          JSONObject pmCallPhoneConfig = new JSONObject();                                                                                                   
          pmCallPhoneConfig.put("itemGroup", "CallPhones");
          pmCallPhoneConfig.put("position", "bottom");//组件显示位置
          callPhoneGroup.put("instanceConfig", pmCallPhoneConfig);                                                                                              
          callPhoneGroup.put("style", "Default");                                                                                                          
          callPhoneGroup.put("defaultOrder", 3);                                                                                                         
          callPhoneGroup.put("separatorFlag", 0); // has separator                                                                                       
          callPhoneGroup.put("separatorHeight", 0);                                                                                                    
          pmGroups.add(callPhoneGroup);
          

//          JSONObject gaLayout = new JSONObject();                                                                                                       
//          gaLayout.put("layoutName", "PmLayout");                                                                                                       
//          gaLayout.put("groups", pmGroups);
//          gaLayout.put("displayName", "物业首页");
//          layouts.add(gaLayout);         
//          root.put("displayName", "物业首页");
//          root.put("layoutName", "PmLayout");                                                                                                       
//          root.put("groups", pmGroups);
          
          
       // 业委会相关layout和group                                                                                                                      
          JSONArray garcGroups = new JSONArray();                                                                                                         
          
          JSONObject garcActionBarGroup = new JSONObject();                                                                                                  
          garcActionBarGroup.put("groupName", "actionBar");                                                                                                           
          garcActionBarGroup.put("widget", "ActionBars");                                                                                                       
          JSONObject garcConfig = new JSONObject();                                                                                                       
          garcConfig.put("itemGroup", "ActionBars");
          garcActionBarGroup.put("instanceConfig", garcConfig);                                                                                                
          garcActionBarGroup.put("style", "Default");                                                                                                        
          garcActionBarGroup.put("defaultOrder", 1);                                                                                                       
          garcActionBarGroup.put("separatorFlag", 0); // no separator                                                                                      
          garcActionBarGroup.put("separatorHeight", 0);                                                                                                     
          garcGroups.add(garcActionBarGroup);    

          JSONObject gracActionGroup = new JSONObject();                                                                                                  
          gracActionGroup.put("groupName", "");                                                                                                           
          pmActionGroup.put("widget", "Navigator");                                                                                                     
          JSONObject gracActionConfig = new JSONObject();                                                                                                 
          gracActionGroup.put("itemGroup", "GaActions");                                                                                                 
          gracActionGroup.put("instanceConfig", gracActionConfig);                                                                                          
          gracActionGroup.put("style", "Metro");                                                                                                           
          gracActionGroup.put("defaultOrder", 2);                                                                                                       
          gracActionGroup.put("separatorFlag", 1); // has separator                                                                                     
          gracActionGroup.put("separatorHeight", 21); 
          gracActionGroup.put("columnCount", 4);
          garcGroups.add(pmActionGroup);                                                                                                                  

          JSONObject gracPostGroup = new JSONObject();                                                                                                    
          gracPostGroup.put("groupName", "");                                                                                                             
          gracPostGroup.put("widget", "Posts");                                                                                                           
          JSONObject gracPostConfig = new JSONObject();                                                                                                   
          gracPostConfig.put("itemGroup", "GaPosts");                                                                                                     
          gracPostGroup.put("instanceConfig", pmPostConfig);                                                                                              
          gracPostGroup.put("style", "Default");                                                                                                          
          gracPostGroup.put("defaultOrder", 3);                                                                                                         
          gracPostGroup.put("separatorFlag", 0); // has separator                                                                                       
          gracPostGroup.put("separatorHeight", 0);                                                                                                    
          garcGroups.add(gracPostGroup);
          
          JSONObject gracCallPhoneGroup = new JSONObject();                                                                                                    
          gracCallPhoneGroup.put("groupName", "CallPhone");                                                                                                             
          gracCallPhoneGroup.put("widget", "CallPhones");                                                                                                           
          JSONObject gracCallPhoneConfig = new JSONObject();                                                                                                   
          gracCallPhoneConfig.put("itemGroup", "CallPhones"); 
          gracCallPhoneConfig.put("position", "bottom");//组件显示位置
          gracCallPhoneGroup.put("instanceConfig", pmCallPhoneConfig);
          gracCallPhoneGroup.put("style", "Default");                                                                                                          
          gracCallPhoneGroup.put("defaultOrder", 3);                                                                                                         
          gracCallPhoneGroup.put("separatorFlag", 0); // has separator                                                                                       
          gracCallPhoneGroup.put("separatorHeight", 0);                                                                                                    
          garcGroups.add(gracCallPhoneGroup);
                                                                                                  
//          root.put("layoutName", "GarcLayout");
//          root.put("displayName", "业委会首页");
//          root.put("groups", garcGroups);
          
          
          
       // 派出所相关layout和group                                                                                                                      
          JSONArray gapsGroups = new JSONArray();                                                                                                         
          
          JSONObject gapsActionBarGroup = new JSONObject();                                                                                                  
          gapsActionBarGroup.put("groupName", "actionBar");                                                                                                           
          gapsActionBarGroup.put("widget", "ActionBars");                                                                                                       
          JSONObject gapsConfig = new JSONObject();                                                                                                       
          gapsConfig.put("itemGroup", "ActionBars");                                                                                                         
          gapsActionBarGroup.put("instanceConfig", gapsConfig);                                                                                                
          gapsActionBarGroup.put("style", "Default");                                                                                                        
          gapsActionBarGroup.put("defaultOrder", 1);                                                                                                       
          gapsActionBarGroup.put("separatorFlag", 0); // no separator                                                                                      
          gapsActionBarGroup.put("separatorHeight", 0);                                                                                                     
          gapsGroups.add(gapsActionBarGroup);    

          JSONObject gapsActionGroup = new JSONObject();                                                                                                  
          gapsActionGroup.put("groupName", "");                                                                                                           
          gapsActionGroup.put("widget", "Navigator");                                                                                                     
          JSONObject gapsActionConfig = new JSONObject();                                                                                                 
          gapsActionGroup.put("itemGroup", "GaActions");                                                                                                 
          gapsActionGroup.put("instanceConfig", gapsActionConfig);                                                                                          
          gapsActionGroup.put("style", "Metro");                                                                                                           
          gapsActionGroup.put("defaultOrder", 2);                                                                                                       
          gapsActionGroup.put("separatorFlag", 1); // has separator                                                                                     
          gapsActionGroup.put("separatorHeight", 21);
          gapsActionGroup.put("columnCount", 4);
          gapsGroups.add(pmActionGroup);                                                                                                                  

          JSONObject gapsPostGroup = new JSONObject();                                                                                                    
          gapsPostGroup.put("groupName", "");                                                                                                             
          gapsPostGroup.put("widget", "Posts");                                                                                                           
          JSONObject gapsPostConfig = new JSONObject();                                                                                                   
          gapsPostConfig.put("itemGroup", "GaPosts");                                                                                                     
          gapsPostGroup.put("instanceConfig", pmPostConfig);                                                                                              
          gapsPostGroup.put("style", "Default");                                                                                                          
          gapsPostGroup.put("defaultOrder", 3);                                                                                                         
          gapsPostGroup.put("separatorFlag", 0); // has separator                                                                                       
          gapsPostGroup.put("separatorHeight", 0);                                                                                                    
          gapsGroups.add(gapsPostGroup);
          
          JSONObject gapsCallPhoneGroup = new JSONObject();                                                                                                    
          gapsCallPhoneGroup.put("groupName", "CallPhone");                                                                                                             
          gapsCallPhoneGroup.put("widget", "CallPhones");
          gapsCallPhoneGroup.put("position", "bottom");//组件显示位置
          JSONObject gapsCallPhoneConfig = new JSONObject();                                                                                                   
          gapsCallPhoneConfig.put("itemGroup", "CallPhones");                                                                                                     
          gapsCallPhoneGroup.put("instanceConfig", gapsCallPhoneConfig);                                                                                              
          gapsCallPhoneGroup.put("style", "Default");                                                                                                          
          gapsCallPhoneGroup.put("defaultOrder", 3);                                                                                                         
          gapsCallPhoneGroup.put("separatorFlag", 0); // has separator                                                                                       
          gapsCallPhoneGroup.put("separatorHeight", 0);                                                                                                    
          gapsGroups.add(gapsCallPhoneGroup);
                                                                                                  
//          root.put("layoutName", "GapsLayout");
//          root.put("displayName", "派出所首页");
//          root.put("groups", gapsGroups);
          
          
          // 居委相关layout和group                                                                                                                      
          JSONArray gancGroups = new JSONArray();                                                                                                         
          
          JSONObject gancActionBarGroup = new JSONObject();                                                                                                  
          gancActionBarGroup.put("groupName", "actionBar");                                                                                                           
          gancActionBarGroup.put("widget", "ActionBars");                                                                                                       
          JSONObject gancConfig = new JSONObject();                                                                                                       
          gancConfig.put("itemGroup", "ActionBars");                                                                                                         
          gancActionBarGroup.put("instanceConfig", gancConfig);                                                                                                
          gancActionBarGroup.put("style", "Default");                                                                                                        
          gancActionBarGroup.put("defaultOrder", 1);                                                                                                       
          gancActionBarGroup.put("separatorFlag", 0); // no separator                                                                                      
          gancActionBarGroup.put("separatorHeight", 0);                                                                                                     
          gancGroups.add(gancActionBarGroup);    

          JSONObject gancActionGroup = new JSONObject();                                                                                                  
          gancActionGroup.put("groupName", "");                                                                                                           
          gancActionGroup.put("widget", "Navigator");                                                                                                     
          JSONObject gancActionConfig = new JSONObject();                                                                                                 
          gancActionGroup.put("itemGroup", "GaActions");                                                                                                 
          gancActionGroup.put("instanceConfig", gancActionConfig);                                                                                          
          gancActionGroup.put("style", "Metro");                                                                                                           
          gancActionGroup.put("defaultOrder", 2);                                                                                                       
          gancActionGroup.put("separatorFlag", 1); // has separator                                                                                     
          gancActionGroup.put("separatorHeight", 21); 
          gancActionGroup.put("columnCount", 4);
          garcGroups.add(pmActionGroup);                                                                                                                  

          JSONObject gancPostGroup = new JSONObject();                                                                                                    
          gancPostGroup.put("groupName", "");                                                                                                             
          gancPostGroup.put("widget", "Posts");                                                                                                           
          JSONObject gancPostConfig = new JSONObject();                                                                                                   
          gancPostConfig.put("itemGroup", "GaPosts");                                                                                                     
          gancPostGroup.put("instanceConfig", pmPostConfig);                                                                                              
          gancPostGroup.put("style", "Default");                                                                                                          
          gancPostGroup.put("defaultOrder", 3);                                                                                                         
          gancPostGroup.put("separatorFlag", 0); // has separator                                                                                       
          gancPostGroup.put("separatorHeight", 0);                                                                                                    
          gancGroups.add(gancPostGroup);
          
          JSONObject gancCallPhoneGroup = new JSONObject();                                                                                                    
          gancCallPhoneGroup.put("groupName", "CallPhone");                                                                                                             
          gancCallPhoneGroup.put("widget", "CallPhones");                                                                                                           
          JSONObject gancCallPhoneConfig = new JSONObject();                                                                                                   
          gancCallPhoneConfig.put("itemGroup", "CallPhones"); 
          gancCallPhoneConfig.put("position", "bottom"); //组件显示位置
          gancCallPhoneGroup.put("instanceConfig", gancCallPhoneConfig);                                                                                              
          gancCallPhoneGroup.put("style", "Default");                                                                                                          
          gancCallPhoneGroup.put("defaultOrder", 3);                                                                                                         
          gancCallPhoneGroup.put("separatorFlag", 0); // has separator                                                                                       
          gancCallPhoneGroup.put("separatorHeight", 0);                                                                                                    
          gancGroups.add(gancCallPhoneGroup);
                                                                                                  
//          root.put("layoutName", "GancLayout");      
//          root.put("displayName", "居委会首页");
//          root.put("groups", gancGroups);
          
          
          //缴费相关layout和group                                                                                                                      
          JSONArray payGroups = new JSONArray();                                                                                                         
          
          JSONObject paymentGroup = new JSONObject();                                                                                                  
          paymentGroup.put("groupName", "pay");                                                                                                           
          paymentGroup.put("widget", "Navigator");                                                                                                       
          JSONObject payConfig = new JSONObject();                                                                                                       
          payConfig.put("itemGroup", "PayActions");                                                                                                         
          paymentGroup.put("instanceConfig", payConfig);                                                                                                
          paymentGroup.put("style", "Light");                                                                                                        
          paymentGroup.put("defaultOrder", 1);                                                                                                       
          paymentGroup.put("separatorFlag", 0); // no separator                                                                                      
          paymentGroup.put("separatorHeight", 0); 
          paymentGroup.put("columnCount", 3);
          payGroups.add(paymentGroup);    

                                                                                                  
//          root.put("layoutName", "PaymentLayout");      
//          root.put("displayName", "缴费首页");
//          root.put("groups", payGroups);
          System.out.println(root.toString());
          return root;
    }
    
    @Test
    public void findLaunchPadLayout(){
        GetLaunchPadLayoutByVersionCodeCommand cmd = new GetLaunchPadLayoutByVersionCodeCommand();
        cmd.setVersionCode(3L);
        cmd.setName("ServiceMarket");
        System.out.println(this.launchPadService.getLastLaunchPadLayoutByVersionCode(cmd));
    }
    
}
