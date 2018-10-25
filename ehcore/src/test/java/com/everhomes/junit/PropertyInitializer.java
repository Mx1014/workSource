// @formatter:off
package com.everhomes.junit;

import com.everhomes.atomikos.AtomikosHelper;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 *
 * Provides mocked property environment for integration tests
 *
 * @author Kelven Yang
 *
 */
public class PropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext applicationContext) {
        AtomikosHelper.fixup();
        try {
            Resource resource = applicationContext.getResource("classpath:ehcore_custom.yml");
            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            PropertySource<?> yamlTestProperties = sourceLoader.load("yamlTestProperties", resource, null);
            applicationContext.getEnvironment().getPropertySources().addFirst(yamlTestProperties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         /*applicationContext.getEnvironment().getPropertySources().addFirst(
           new MockPropertySource()
            .withProperty("redis.bus.host", "redis-server")
            .withProperty("redis.bus.port", "6379")
            .withProperty("redis.store.master.host", "redis-server")
            .withProperty("redis.store.master.port", "6380")
            .withProperty("upload.max.size", 100000)
            .withProperty("heartbeat.interval", 10000)
            .withProperty("scheduler.pool.size", 100)
            .withProperty("messaging.msgbox.size", 10)
            .withProperty("messaging.batch.size", 10)
            .withProperty("messaging.routing.block.size", 10)
            .withProperty("forum.postbox.size", 10)
            .withProperty("forum.flush.interval", 3000)
            .withProperty("db.master", "jdbc:mysql://ehcore:ehcore@10.1.10.81:3306/ehcore")
            .withProperty("db.conn.pool", 8)
            .withProperty("db.tx.timeout", 600)
            .withProperty("db.jta", false)
            .withProperty("db.conn.borrow.timeout", 60)
            .withProperty("serialization.serializable", "EvhJsonSerializable")
            .withProperty("serialization.helper", "EvhJsonSerializationHelper")
            .withProperty("objc.header.ext", ".h")
            .withProperty("objc.source.ext", ".m")
            .withProperty("objc.response.base", "EvhRestResponseBase")
            .withProperty("class.name.prefix", "Evh")
            .withProperty("source.jars", "")
            .withProperty("source.excludes", "com.everhomes.rpcapi.*")
            .withProperty("destination.dir", "/")
            .withProperty("destination.dir.java", "/")
            .withProperty("javadoc.root", "/")
            .withProperty("spring.freemarker.check-template-location", false)
            .withProperty("db.driver", "com.mysql.jdbc.Driver")
            .withProperty("elastic.nodes.hosts", "elasticsearch")
            .withProperty("elastic.nodes.ports", "9300")
            .withProperty("elastic.index", "everhomesv3")
            .withProperty("javadoc.root", "http://localhost:8080/apidocs")
            .withProperty("javadoc.location", "file:///Users/kelveny/archive/ehng/apidocs/")
            .withProperty("web.location", "/resources/")
            .withProperty("apns.certname", "apns-develop")
            .withProperty("biz.serverUrl", "http://biz.zuolin.com/zl-ec")
            .withProperty("biz.appKey", "39628d1c-0646-4ff6-9691-2c327b03f9c4")
            .withProperty("biz.secretKey", "PSsIB9nZm3ENS3stei8oAvGa2afRW7wT+Y6x76XDtUCUcXOUhkPYK9V/5r03pD2rquQ==")
            .withProperty("elastic.nodes.httpports", "9200")
            .withProperty("equipment.ip", "127.0.0.1")
            .withProperty("schedule.running.flag", "0")
            .withProperty("src.path", "/home/janson/ssd2/everhomes/ehnextgen")
            .withProperty("schedule.running.flag", "1")
            );*/
    }
}
