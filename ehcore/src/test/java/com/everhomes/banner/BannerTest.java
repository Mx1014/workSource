// @formatter:off
package com.everhomes.banner;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.banner.admin.CreateBannerAdminCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.ItemGroup;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BannerTest extends CoreServerTestCase {
    
    @Autowired
    private BannerService bannerService;
    
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
        CreateBannerAdminCommand cmd = new CreateBannerAdminCommand();
        cmd.setBannerGroup(ItemGroup.BIZS.getCode());
        cmd.setBannerLocation("/home");
//        cmd.setActionName("google");
//        cmd.setActionUri("www.google.com");
        cmd.setAppid(0L);
        cmd.setName("谷歌");
        cmd.setNamespaceId(0);
        cmd.setOrder(0);
        BannerScope scope = new BannerScope();
        scope.setOrder(0);
        scope.setScopeId(0L);
        scope.setScopeCode(ScopeType.ALL.getCode());
        List<BannerScope> scopes = new ArrayList<BannerScope>();
        scopes.add(scope);
        cmd.setScopes(scopes);
        cmd.setStatus(BannerStatus.ACTIVE.getCode());
        bannerService.createBanner(cmd);
    }

   
}
