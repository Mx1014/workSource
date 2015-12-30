package com.everhomes.business;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.core.joran.action.ActionUtil.Scope;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.business.BusinessScope;
import com.everhomes.rest.business.CancelFavoriteBusinessCommand;
import com.everhomes.rest.business.FavoriteBusinessCommand;
import com.everhomes.rest.business.GetBusinessesByCategoryCommand;
import com.everhomes.rest.business.GetBusinessesByCategoryCommandResponse;
import com.everhomes.rest.business.SyncBusinessCommand;
import com.everhomes.rest.business.SyncDeleteBusinessCommand;
import com.everhomes.rest.business.UpdateBusinessCommand;
import com.everhomes.rest.business.admin.PromoteBusinessAdminCommand;
import com.everhomes.rest.business.admin.RecommendBusinessesAdminCommand;
import com.everhomes.rest.launchpad.ItemScope;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;

public class BusinessTest extends CoreServerTestCase {
    
    @Autowired
    private BusinessService businessService;
    @Before
    public void setup() {
        User user = new User();
        user.setId(152719L);
        UserContext.current().setUser(user);
    }
    
    //@After
    public void teardown() {
       
    }
    
    @Test
    public void testCreateBusiness(){
        SyncBusinessCommand cmd = new SyncBusinessCommand();
        cmd.setUserId(152719L);;
        cmd.setAddress("深圳");
        cmd.setBizOwnerUid(152719L);
        List<Long> categroies = new ArrayList<Long>();
        categroies.add(3001L);
        //categroies.add(3002L);
        cmd.setCategroies(categroies);
        cmd.setTargetId("1009");
        cmd.setContact("15875300001");
        cmd.setDisplayName("水管水管第三帝国");
        cmd.setLatitude(23.123);
        cmd.setLongitude(108.123);
        cmd.setName("safsdfsadf");
        cmd.setPhone("15875300001");
        cmd.setTargetType((byte)2);
        //cmd.setTargetId("1006");
        cmd.setUrl("http://www.baidu.com");
        List<BusinessScope> scopes = new ArrayList<>();
        BusinessScope scope = new BusinessScope();
        scope.setScopeId(0L);
        scope.setScopeCode((byte)0);
        scopes.add(scope);
        //cmd.setScopes(scopes);
        cmd.setDescription("xxxxxxxxxxxx");
        businessService.syncBusiness(cmd);
    }
    @Test
    public void testGetBusinessByCategory(){
        GetBusinessesByCategoryCommand cmd = new GetBusinessesByCategoryCommand();
        cmd.setCategoryId(3001L);
        GetBusinessesByCategoryCommandResponse response = businessService.getBusinessesByCategory(cmd);
        if(response.getRequests() != null)
            System.out.println(response.getRequests());
    }
    
    @Test
    public void testUpdateBusiness(){
        UpdateBusinessCommand cmd = new UpdateBusinessCommand();
        cmd.setAddress("深圳");
        cmd.setId(5L);
        List<Long> categroies = new ArrayList<Long>();
        categroies.add(3003L);
        //categroies.add(3002L);
        cmd.setCategroies(categroies);
        cmd.setContact("15875300001");
        cmd.setDisplayName("金融xx");
        cmd.setLatitude(23.123);
        cmd.setLongitude(108.123);
        cmd.setName("金融xx");
        cmd.setPhone("15875300001");
        cmd.setTargetType((byte)2);
        cmd.setTargetId("");
        cmd.setUrl("http://www.baidu.com");
        List<BusinessScope> scopes = new ArrayList<>();
        BusinessScope scope = new BusinessScope();
        scope.setScopeId(0L);
        scope.setScopeCode((byte)0);
        scopes.add(scope);
        //cmd.setScopes(scopes);
        cmd.setDescription("xxxxxxxxxxxx");
        businessService.updateBusiness(cmd);
    }
    @Test
    public void testRecommandBusiness(){
        RecommendBusinessesAdminCommand cmd = new RecommendBusinessesAdminCommand();
        cmd.setId(14L);
        BusinessScope scope = new BusinessScope();
        scope.setScopeId(0l);
        scope.setScopeCode((byte)0);
        List<BusinessScope> scopes = new ArrayList<>();
        scopes.add(scope);
        cmd.setScopes(scopes);
        businessService.recommendBusiness(cmd);
    }
    
    @Test
    public void testDeleteBusiness(){
        SyncDeleteBusinessCommand cmd = new SyncDeleteBusinessCommand();
        cmd.setId(String.valueOf("14417668830919345118"));
        cmd.setUserId(152719L);
        businessService.syncDeleteBusiness(cmd);
    }
    
    @Test
    public void testPromoteBusiness(){
        PromoteBusinessAdminCommand cmd = new PromoteBusinessAdminCommand();
        cmd.setId(34L);
        ItemScope scope = new ItemScope();
        scope.setScopeId(510635L);
        scope.setScopeCode((byte)0);
        List<ItemScope> scopes = new ArrayList<>();
        scopes.add(scope);
        
        ItemScope scope1 = new ItemScope();
        scope1.setScopeId(510636L);
        scope1.setScopeCode((byte)0);
        scopes.add(scope1);
        cmd.setItemScopes(scopes);
        businessService.promoteBusiness(cmd);
    }
    
    @Test
    public void testFavoriteBusiness(){
        FavoriteBusinessCommand cmd = new FavoriteBusinessCommand();
        cmd.setId(2L);
        businessService.favoriteBusiness(cmd);
        
    }
    
    @Test
    public void testCancelFavoriteBusiness(){
        CancelFavoriteBusinessCommand cmd = new CancelFavoriteBusinessCommand();
        cmd.setId(2L);
        businessService.cancelFavoriteBusiness(cmd);
        
    }
}
