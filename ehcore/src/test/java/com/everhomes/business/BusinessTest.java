package com.everhomes.business;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;

public class BusinessTest extends CoreServerTestCase {
    
    @Autowired
    private BusinessService businessService;
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
    public void testCreateBusiness(){
        CreateBusinessCommand cmd = new CreateBusinessCommand();
        cmd.setAddress("深圳");
        cmd.setBizOwnerUid(1001l);
        List<Long> categroies = new ArrayList<Long>();
        categroies.add(3001L);
        //categroies.add(3002L);
        cmd.setCategroies(categroies);
        cmd.setContact("15875300001");
        cmd.setDisplayName("金融");
        cmd.setLatitude(23.123);
        cmd.setLongitude(108.123);
        cmd.setName("金融");
        cmd.setPhone("15875300001");
        cmd.setTargetType((byte)2);
        cmd.setTargetId("");
        cmd.setUrl("http://www.baidu.com");
        List<BusinessScope> scopes = new ArrayList<>();
        BusinessScope scope = new BusinessScope();
        scope.setScopeId(0L);
        scope.setScopeType((byte)0);
        scopes.add(scope);
        cmd.setScopes(scopes);
        cmd.setDescription("xxxxxxxxxxxx");
        businessService.createBusiness(cmd);
    }
    @Test
    public void testGetBusinessByCategory(){
        GetBusinessesByCategoryCommand cmd = new GetBusinessesByCategoryCommand();
        cmd.setCategoryId(3001L);
        List<BusinessDTO> businessDTOs = businessService.getBusinessesByCategory(cmd);
        if(businessDTOs != null)
            System.out.println(businessDTOs);
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
        scope.setScopeType((byte)0);
        scopes.add(scope);
        //cmd.setScopes(scopes);
        cmd.setDescription("xxxxxxxxxxxx");
        businessService.updateBusiness(cmd);
    }
}
