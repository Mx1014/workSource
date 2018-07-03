package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>id: 模块应用id</li>
 *     <li>originId: 应用跨版本不变id</li>
 *     <li>name: 模块应用名称</li>
 *     <li>moduleId: 模块id</li>
 *     <li>moduleName: 模块名称</li>
 *     <li>actionType: actionType</li>
 *     <li>instanceConfig: 参数json</li>
 *     <li>menuId: 关联的菜单id</li>
 *     <li>moduleControlType: 模块控制类型 参考{@link com.everhomes.rest.oauth2.ModuleManagementType}</li>
 *     <li>accessControlType: 权限控制类型 参考{@link com.everhomes.rest.module.AccessControlType}</li>
 * </ul>
 */
public class ServiceModuleAppDTO {

    private Long id;
    private Long originId;
    private String name;
    private Long moduleId;
    private String moduleName;
    private Byte actionType;
    private String instanceConfig;
    private Long menuId;
    private String moduleControlType;



    private Byte accessControlType;

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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getModuleControlType() {
        return moduleControlType;
    }

    public void setModuleControlType(String moduleControlType) {
        this.moduleControlType = moduleControlType;
    }

    public Byte getAccessControlType() {
        return accessControlType;
    }

    public void setAccessControlType(Byte accessControlType) {
        this.accessControlType = accessControlType;
    }
}
