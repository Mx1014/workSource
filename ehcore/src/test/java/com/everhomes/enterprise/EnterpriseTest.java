package com.everhomes.enterprise;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.ConvertHelper;

public class EnterpriseTest extends LoginAuthTestCase {
    @Autowired
    EnterpriseProvider enterpriseProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
        })
    static class ContextConfiguration {
    }
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() {
        
    }
    
    //@Test
    public void testEnterpriseCommunity() {
        Community ec1 = communityProvider.findCommunityById(24206890946790401l);
        Assert.assertTrue(ec1 != null);
        
        EnterpriseCommunity ec2 = ConvertHelper.convert(ec1, EnterpriseCommunity.class);
        ec2.setId(0l);
        enterpriseProvider.createEnterpriseCommunity(1l, ec2);
        Assert.assertTrue(ec2.getId() > 0);
        
    }
    
    @Test
    public void testEnterprise() {
        Enterprise enterprise = new Enterprise();
        enterprise.setName("永佳天成科技发展有限公司");
        enterprise.setDescription("仅仅为测试需要");
        enterpriseProvider.createEnterprise(enterprise);
        Assert.assertTrue(enterprise.getId() > 0);
        
        Enterprise e2 = this.enterpriseProvider.getEnterpriseById(enterprise.getId());
        Assert.assertTrue(e2 != null);
        
        this.enterpriseProvider.deleteEnterpriseById(e2.getId());
        Enterprise e3 = this.enterpriseProvider.getEnterpriseById(e2.getId());
        Assert.assertTrue(e3 == null);
        
        EnterpriseCommunityMap ep = new EnterpriseCommunityMap();
        
        
    }
}
