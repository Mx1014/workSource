// @formatter:off
package com.everhomes.pm;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
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
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class PropertyProviderTest  extends TestCase {
     
    @Autowired
    PropertyMgrProvider pp;
    
    @Autowired
    PropertyMgrService ps;
    
    List<Long> newRecordIds = new ArrayList<Long>();
    
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
       
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testListAddressMappings() {
    	ListPropAddressMappingCommand cmd = new ListPropAddressMappingCommand();
    	cmd.setCommunityId(15l);
    	cmd.setPageOffset(1);
    	cmd.setPageSize(20);
		ListPropAddressMappingCommandResponse response = ps.ListAddressMappings(cmd );
		List<PropAddressMappingDTO> list = response.getMembers();
		for (PropAddressMappingDTO dto : list) {
			System.out.println(dto);
		}
    }
    
    
}
