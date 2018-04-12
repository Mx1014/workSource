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
 *     <li>orgAppId: 公司安装app的id</li>
 *     <li>appType: appType应用类型 0-oa, 1-园区, 2-服务{@link com.everhomes.rest.module.ServiceModuleAppType}</li>
 *     <li>description: 应用描述</li>
 *     <li>version: 版本信息</li>
 *     <li>appCategory: 应用分类信息</li>
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
    private Long orgAppId;
    private Byte appType;
    private String description;
    private String version;
    private String appCategory;
    private Byte mobileFlag;
    private Byte pcFlag;
    private String mobileUri;
    private String pcUri;
    private String appEntryInfo;
    private Byte independentConfigFlag;
    private String configAppIds;
    private Byte supportThirdFlag;
    private Byte defaultFlag;


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

    public Long getOrgAppId() {
        return orgAppId;
    }

    public void setOrgAppId(Long orgAppId) {
        this.orgAppId = orgAppId;
    }

    public Byte getAppType() {
        return appType;
    }

    public void setAppType(Byte appType) {
        this.appType = appType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }

    public Byte getMobileFlag() {
        return mobileFlag;
    }

    public void setMobileFlag(Byte mobileFlag) {
        this.mobileFlag = mobileFlag;
    }

    public Byte getPcFlag() {
        return pcFlag;
    }

    public void setPcFlag(Byte pcFlag) {
        this.pcFlag = pcFlag;
    }

    public String getMobileUri() {
        return mobileUri;
    }

    public void setMobileUri(String mobileUri) {
        this.mobileUri = mobileUri;
    }

    public String getPcUri() {
        return pcUri;
    }

    public void setPcUri(String pcUri) {
        this.pcUri = pcUri;
    }

    public String getAppEntryInfo() {
        return appEntryInfo;
    }

    public void setAppEntryInfo(String appEntryInfo) {
        this.appEntryInfo = appEntryInfo;
    }

    public Byte getIndependentConfigFlag() {
        return independentConfigFlag;
    }

    public void setIndependentConfigFlag(Byte independentConfigFlag) {
        this.independentConfigFlag = independentConfigFlag;
    }

    public String getConfigAppIds() {
        return configAppIds;
    }

    public void setConfigAppIds(String configAppIds) {
        this.configAppIds = configAppIds;
    }

    public Byte getSupportThirdFlag() {
        return supportThirdFlag;
    }

    public void setSupportThirdFlag(Byte supportThirdFlag) {
        this.supportThirdFlag = supportThirdFlag;
    }

    public Byte getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(Byte defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
