package com.everhomes.portal;

/**
 * Created by sfyan on 2017/8/8.
 */
public interface PortalPublishHandler {

    String PORTAL_PUBLISH_OBJECT_PREFIX = "PortalPublishModuleId-";

    /**
     * 发布具体模块的内容
     * 注：instanceConfig就是跟web端协商好的json字符串，需要解析后添加或者修改（数据是否有id来判断添加或者修改）到对应的业务表里面去，如果是对应的配置项要添加到业务表，则会生成id，需要把生成的id字段回填到instanceConfig，再次发布的时候能通过id找到对应的业务数据，然后进行修改，如果是修改，则通过id直接找到对应业务数据修改即可
     * @param instanceConfig 具体模块配置的参数
     * @return instanceConfig 把json对象里面个个实体需要的id补充返回
     */
    String publish(Integer namespaceId, String instanceConfig,String itemLabel);

    /**
     * 处理成服务广场item需要的actionData
     * 注：把instanceConfig中复杂的数据处理成服务广场需要的actionData字符串
     * @param instanceConfig
     * @return
     */
    String getItemActionData(Integer namespaceId, String instanceConfig);

    /**
     * 根据服务广场的actionData获取业务应用的instanceConfig
     * 注：通过actionData找到对应的所有需要配置的数据组装成之前跟web端人员协商好的instanceConfig，后面用于给web端人员解析展示对应到页面的各个配置
     * @param actionData
     * @return
     */
    String getAppInstanceConfig(Integer namespaceId, String actionData);

    String processInstanceConfig(String instanceConfig);
}
