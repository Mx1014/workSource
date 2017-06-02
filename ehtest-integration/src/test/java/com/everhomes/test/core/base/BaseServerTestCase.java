// @formatter:off
package com.everhomes.test.core.base;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.everhomes.test.core.persist.PersistConfig;
import com.everhomes.test.core.redis.RedisConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
    classes={BaseServerConfig.class, PersistConfig.class, RedisConfig.class})
public class BaseServerTestCase extends TestCase {
    @Value("${namespace.id}")
    protected Integer namespaceId;
}
