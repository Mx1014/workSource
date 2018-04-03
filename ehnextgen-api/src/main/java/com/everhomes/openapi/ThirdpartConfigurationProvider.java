package com.everhomes.openapi;

/**
 * Created by ying.xiong on 2017/8/11.
 */
public interface ThirdpartConfigurationProvider {

    ThirdpartConfiguration findByName(String name, Integer namespaceId);
}
