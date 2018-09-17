package com.everhomes.eureka;

import com.netflix.discovery.EurekaClientConfig;
import org.elasticsearch.common.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * A default implementation of eureka client configuration as required by
 * {@link EurekaClientConfig}.
 *
 * <p>
 * The information required for configuring eureka client is provided in a
 * configuration file.The configuration file is searched for in the classpath
 * with the name specified by the property <em>eureka.client.props</em> and with
 * the suffix <em>.properties</em>. If the property is not specified,
 * <em>eureka-client.properties</em> is assumed as the default.The properties
 * that are looked up uses the <em>namespace</em> passed on to this class.
 * </p>
 *
 * <p>
 * If the <em>eureka.environment</em> property is specified, additionally
 * <em>eureka-client-<eureka.environment>.properties</em> is loaded in addition
 * to <em>eureka-client.properties</em>.
 * </p>
 *
 * @author Karthik Ranganathan
 *
 */
@Singleton
// @ProvidedBy(DefaultEurekaClientConfigProvider.class)
public class DefaultEurekaClientConfig extends com.netflix.discovery.DefaultEurekaClientConfig {

    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {
        List<String> list = new ArrayList<>(1);
        list.add("http://localhost:8761/eureka/");
        return list;
    }
}

