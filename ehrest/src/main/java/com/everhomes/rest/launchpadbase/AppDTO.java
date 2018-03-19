package com.everhomes.rest.launchpadbase;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 模块应用id</li>
 *     <li>originId: 应用跨版本不变id</li>
 *     <li>name: 模块应用名称</li>
 *     <li>moduleId: 模块id</li>
 *     <li>instanceConfig: 参数json</li>
 * </ul>
 */
public class AppDTO {

    private Long id;
    private Long originId;
    private String name;
    private Long moduleId;
    private String instanceConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
