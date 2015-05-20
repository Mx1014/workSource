// @formatter:off
package com.everhomes.pm;

import java.util.List;

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

import com.everhomes.db.DbProvider;
import com.everhomes.junit.PropertyInitializer;
import com.everhomes.sharding.ShardingProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class PropertyTest {
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private PropertyMgrProvider pp;
    
    @Autowired
    private PropertyMgrService ps;

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

    @Test
    public void testtListPropPmOwners() {
    	List<CommunityPmOwner> list = pp.listCommunityPmOwners(12l, null, null, 1, 20);
    	for (CommunityPmOwner communityPmOwner : list) {
			System.out.println(communityPmOwner);
		}
    }
    
    @Test
    public void testtListPropPmOwners2() {
    	ListPropOwnerCommand cmd = new ListPropOwnerCommand();
    	cmd.setCommunityId(12l);
    	cmd.setPageOffset(1);
    	ListPropOwnerCommandResponse response= ps.listPMPropertyOwnerInfo(cmd);
    	List<PropOwnerDTO> list = response.getMembers();
    	for (PropOwnerDTO propOwnerDTO : list) {
			System.out.println(propOwnerDTO);
		}
    }
    
    
    @Test
    public void testListPropAddressMapping() {
    	List<CommunityAddressMapping> list = pp.listCommunityAddressMappings(12l);
    	for (CommunityAddressMapping communityPmOwner : list) {
			System.out.println(communityPmOwner);
		}
    }
    
    @Test
    public void testinsertBill () {
    	CommunityAddressMapping mapping = new CommunityAddressMapping();
    	mapping.setAddressId(122356l);
    	mapping.setCommunityId(12l);
    	mapping.setName("测试");
    	mapping.setLivingStatus((byte)2);
    	pp.createPropAddressMapping(mapping);
    	System.out.println(mapping.getId());
    	
//    	CommunityAddressMapping mapping2 = new CommunityAddressMapping();
//    	mapping2.setAddressId(122356l);
//    	mapping2.setCommunityId(12l);
//    	mapping2.setName("测试2");
//    	mapping2.setLivingStatus((byte)3);
//    	pp.createPropAddressMapping2(mapping2);
//    	System.out.println(mapping2.getId());
    }
    
    @Test
    public void testinsertBill2 () {
    	
    	ListPropBillCommand cmd = new ListPropBillCommand();
    	cmd.setCommunityId(12l);
    	cmd.setPageOffset(1);
    	cmd.setAddress("");
    	cmd.setDateStr("");
//    	cmd.set
		ListPropBillCommandResponse commandResponse = ps.listPropertyBill(cmd );
		List<PropBillDTO> list = commandResponse.getMembers();
		for (PropBillDTO propBillDTO : list) {
			System.out.println(propBillDTO);
		}
    }
   
   
}
