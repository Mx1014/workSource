package com.everhomes.search;

import java.util.List;

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

import com.everhomes.db.DbProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.junit.PropertyInitializer;
import com.everhomes.sharding.ShardingProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class GroupSearchTest {
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private GroupProvider groupProvider;

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
    
    @Ignore @Test
    public void testGroupCRUD() {
        Group group = new Group();
        group.setName("group 1");
        group.setCreatorUid(1L);
        this.groupProvider.createGroup(group);
        
        //groupProvider.listGroupMembers(locator, count)
        
        for(int i = 0; i < 100; i++) {
            GroupMember member = new GroupMember();
            member.setGroupId(group.getId());
            member.setMemberType("User");
            member.setMemberId((long)(i + 1));
            this.groupProvider.createGroupMember(member);
        }
    }
    
}
