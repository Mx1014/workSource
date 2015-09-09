package com.everhomes.forum;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.junit.PropertyInitializer;
import com.everhomes.schema.Tables;
import com.everhomes.sharding.Server;
import com.everhomes.sharding.ServerStatus;
import com.everhomes.sharding.ServerType;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.DateHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = { PropertyInitializer.class },
    loader = AnnotationConfigContextLoader.class)
public class ForumTest {

    @Autowired
    ForumProviderImpl forumProvider;
    
    @Autowired
    ShardingProvider shardingProvider;
    
    @Value("${redis.bus.host}")
    String redisHost;
    
    @Value("${redis.bus.port}")
    int redisPort;
    
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
    
    @Ignore @Test
    public void setup() {
        Server server;
        
        server = new Server();
        server.setAddressUri("redis-server");
        server.setAddressPort(6379);
        server.setServerType(ServerType.RedisStorage.ordinal());
        server.setStatus(ServerStatus.enabled.ordinal());
        shardingProvider.createServer(server);
        
        server = new Server();
        server.setAddressUri("redis-server");
        server.setAddressPort(6379);
        server.setServerType(ServerType.RedisCache.ordinal());
        server.setStatus(ServerStatus.enabled.ordinal());
        shardingProvider.createServer(server);
    }
    
    @Ignore @Test
    public void testPostInsertion() {
        setup();
        
        // create forum
        Forum forum = new Forum();
        forum.setAppId(0L);
        Timestamp ts = new Timestamp(DateHelper.currentGMTTime().getTime());
        forum.setCreateTime(ts);
        forum.setDescription("a test forum");
        forum.setModifySeq(1L);
        forum.setName("forum");
        forum.setOwnerType("group");
        forum.setOwnerId(1L);
        forum.setPostCount(0L);
        forum.setUpdateTime(ts);
        forumProvider.createForum(forum);
        
        // create root post
        Post post = new Post();
        post.setAppId(0L);
        post.setChildCount(0L);
        post.setCreateTime(ts);
        post.setCreatorUid(1L);
        post.setForumId(forum.getId());
        long seq = 1;
        post.setModifySeq(seq);
        post.setLikeCount(0L);
        post.setUpdateTime(ts);
        forumProvider.createPost(post);

        seq++;
        long parentPostId = post.getId();
        
        // create reply post
        for(int i = 0; i < 100; i++, seq++) {
            post = new Post();
            post.setAppId(0L);
            post.setChildCount(0L);
            post.setCreateTime(ts);
            post.setCreatorUid(1L);
            post.setForumId(forum.getId());
            post.setModifySeq(seq);
            post.setLikeCount(0L);
            post.setUpdateTime(ts);
            post.setParentPostId(parentPostId);
            forumProvider.createPost(post);
            
            Attachment attachment = new Attachment();
            attachment.setContentType("image/jpeg");
            attachment.setContentUri("/post-" + post.getId() + "/image1.jpg");
            attachment.setCreatorUid(1L);
            attachment.setPostId(post.getId());
            attachment.setCreateTime(ts);
            forumProvider.createPostAttachment(attachment);
            
            attachment = new Attachment();
            attachment.setContentType("image/jpeg");
            attachment.setContentUri("/post-" + post.getId() + "/image2.jpg");
            attachment.setCreatorUid(1L);
            attachment.setPostId(post.getId());
            attachment.setCreateTime(ts);
            forumProvider.createPostAttachment(attachment);
            
            attachment = new Attachment();
            attachment.setContentType("image/jpeg");
            attachment.setContentUri("/post-" + post.getId() + "/image3.jpg");
            attachment.setCreatorUid(1L);
            attachment.setPostId(post.getId());
            attachment.setCreateTime(ts);
            forumProvider.createPostAttachment(attachment);
        }
    }
}
