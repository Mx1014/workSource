package com.everhomes.portal;

import com.everhomes.serviceModuleApp.ServiceModuleApp;

/**
 * Created by sfyan on 2017/8/8.
 */
public interface PortalPublishHandler {

    String PORTAL_PUBLISH_OBJECT_PREFIX = "PortalPublishModuleId-";

    /**
     * 发布应用接口，用于发布
     * 注：在发布应用的时候会调用此接口，把应用的配置参数作为参数传来，然后业务可以自己处理此参数并返回，最终运营后台会将此参数更新到应用表中。例如活动类型的应用需要加一个categoryId在配置里面。
     * @param instanceConfig 应用的配置参数
     * @return instanceConfig 业务自己处理过后的应用配置参数
     */
    String publish(Integer namespaceId, String instanceConfig,String appName);

    /**
     * 填充应用的信息，比如说是icon的url等，用于查询等
     * 注：去查询应用的时候会将config传给业务，让业务填充一些配置信息返回，类似于group变成groupDTO对象的过程，运营后台不会更新此参数到应用表中。
     * @param instanceConfig
     * @return
     */
    String processInstanceConfig(Integer namespaceId,String instanceConfig);


    /**
     * 处理成服务广场item需要的actionData，用于发布
     * 注：把应用配置instanceConfig中复杂的数据处理成服务广场需要的actionData字符串
     * @param instanceConfig
     * @return
     */
    String getItemActionData(Integer namespaceId, String instanceConfig);

    /**
     * 根据服务广场的actionData获取业务应用的instanceConfig，用于同步
     * 注：通过广场图标的actionData转换成应用的配置instanceConfig
     * @param actionData
     * @return
     */
    String getAppInstanceConfig(Integer namespaceId, String actionData);

    /**
     * 根据应用信息，传回多入口的标志ID，用于同步、发布
     * @param namespaceId
     * @param moudleId
     * @param instanceConfig
     * @return
     */
    default String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig){
        return null;
    }

    /**
     * 根据应用信息，传回多入口的菜单Id，此接口其实已经废弃。
     * @param namespaceId
     * @param moudleId
     * @param instanceConfig
     * @return
     */
    default Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig){
        return null;
    }
    
    /**
     * 所有应用发布完成之后，再给各个应用发送一个通知
     * @param app
     */
    default void afterAllAppPulish(ServiceModuleApp app){
    	
    }
}
