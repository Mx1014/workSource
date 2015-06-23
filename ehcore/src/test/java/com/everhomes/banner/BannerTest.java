// @formatter:off
package com.everhomes.banner;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
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
import com.everhomes.launchpad.ItemGroup;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class BannerTest extends TestCase {
    
    @Autowired
    private BannerService bannerService;
    
    
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
        User user = new User();
        user.setId(10001L);
        UserContext.current().setUser(user);
    }
    
    //@After
    public void teardown() {
       
    }
    
    @Test
    public void testCreateBanner() {
        CreateBannerCommand cmd = new CreateBannerCommand();
        cmd.setBannerGroup(ItemGroup.BIZS.getCode());
        cmd.setBannerLocation("/home");
        cmd.setActionName("google");
        cmd.setActionUri("www.google.com");
        cmd.setAppid(0L);
        cmd.setName("谷歌");
        cmd.setNamespaceId(0);
        cmd.setOrder(0);
        BannerScope scope = new BannerScope();
        scope.setOrder(0);
        scope.setScopeId(0L);
        scope.setScopeType(BannerScopeType.COUNTRY.getCode());
        List<BannerScope> scopes = new ArrayList<BannerScope>();
        scopes.add(scope);
        cmd.setScopes(scopes);
        cmd.setStatus(BannerStatus.ACTIVE.getCode());
        bannerService.createBanner(cmd);
    }

   
}
