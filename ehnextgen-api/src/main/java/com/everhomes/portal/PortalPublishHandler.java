package com.everhomes.portal;

/**
 * Created by sfyan on 2017/8/8.
 */
public interface PortalPublishHandler {

    String PORTAL_PUBLISH_OBJECT_PREFIX = "PortalPublishModuleId-";

    /**
     * 发布具体模块的内容
     * @param instanceConfig 具体模块配置的参数
     * @return 服务广场item的actionData
     */
    String publish(String instanceConfig);
}
