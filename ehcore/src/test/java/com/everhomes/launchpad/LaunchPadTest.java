// @formatter:off
package com.everhomes.launchpad;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

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
        
        cmd.setItemGroup(ItemGroup.PROPERTY.getCode());
        cmd.setItemLabel("物业建议");
        cmd.setItemName(CategoryType.ADVISE.getCode());
        cmd.setAppId(AppConstants.APPID_PM);
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
        item1.setId(1L);
        item1.setOrderIndex(0);
        
//        Item item2  = new Item();
//        item2.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
//        item2.setId(2L);
//        item2.setOrderIndex(0);
        
        Item item3  = new Item();
        item3.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
        item3.setId(6L);
        item3.setOrderIndex(0);
        
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
        items.add(item5);
        items.add(item6);
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
    
   
}
