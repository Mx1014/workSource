package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>id: 模块应用id</li>
 * <li>name: 模块应用名称</li>
 * <li>moduleId: 模块id</li>
 * <li>moduleName: 模块名称</li>
 * <li>instanceConfig: 参数json</li>
 * </ul>
 */
public class ServiceModuleAppDTO {

    private Long id;
    private String name;
    private Long moduleId;
    private Long moduleName;
    private String instanceConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getModuleName() {
        return moduleName;
    }

    public void setModuleName(Long moduleName) {
        this.moduleName = moduleName;
    }

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }
}
