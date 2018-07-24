package com.everhomes.rest.portal;

import com.everhomes.rest.acl.AppEntryInfoDTO;
import com.everhomes.rest.acl.ServiceModuleEntryDTO;

import java.util.List;

/**
 * <ul>
 *     <li>id: 模块应用id</li>
 *     <li>originId: 应用跨版本不变id</li>
 *     <li>name: 模块应用名称</li>
 *     <li>moduleId: 模块id</li>
 *     <li>moduleName: 模块名称</li>
 *     <li>clientHandlerType: 客户端处理方式 参考{@link ClientHandlerType}</li>
 *     <li>actionType: actionType</li>
 *     <li>instanceConfig: 参数json</li>
 *     <li>menuId: 关联的菜单id</li>
 *     <li>moduleControlType: 模块控制类型 参考{@link com.everhomes.rest.oauth2.ModuleManagementType}</li>
 *     <li>orgAppId: 公司安装app的id</li>
 *     <li>status: 启用状态{@link com.everhomes.rest.servicemoduleapp.OrganizationAppStatus}</li>
 *     <li>appType: appType应用类型 0-oa, 1-园区, 2-服务{@link com.everhomes.rest.module.ServiceModuleAppType}</li>
 *     <li>profileId: 应用描述id</li>
 *     <li>description: 应用描述</li>
 *     <li>displayVersion: displayVersion</li>
 *     <li>appNo: 应用编号</li>
 *     <li>mobileFlag: 支持移动端</li>
 *     <li>pcFlag: 支持PC端</li>
 *     <li>mobileUris: mobileUris</li>
 *     <li>mobileUrls: mobileUrls</li>
 *     <li>pcUris: pcUris</li>
 *     <li>pcUrls: pcUrls</li>
 *     <li>appEntryInfos: 应用entry信息</li>
 *     <li>independentConfigFlag: 允许独立配置</li>
 *     <li>dependentAppIds: 配置应用id</li>
 *     <li>dependentAppNames: 配置应用name</li>
 *     <li>supportThirdFlag: 支持第三方对接</li>
 *     <li>iconUri: iconUri</li>
 *     <li>iconUrl: iconUrl</li>
 *     <li>systemAppFlag: 是否为系统应用，例如“系统管理员”是系统应用，是模块的属性。0-no, 1-yes。参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>defaultAppFlag: 是否为默认安装应用，可以动态设置。0-no, 1-yes。参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>serviceModuleEntryDtos: 应用入口信息参考{@link ServiceModuleEntryDTO}</li>
 *     <li>accessControlType: 权限控制类型 参考{@link com.everhomes.rest.module.AccessControlType}</li>
 * </ul>
 */
public class ServiceModuleAppDTO {

    private Long id;
    private Long originId;
    private String name;
    private Long moduleId;
    private String moduleName;
    private Byte clientHandlerType;
    private Byte actionType;
    private String instanceConfig;
    private Long menuId;
    private String moduleControlType;
    private Long orgAppId;
    private Byte status;
    private Byte appType;
    private Long profileId;
    private String description;
    private String displayVersion;
    private String appNo;
    private Byte mobileFlag;
    private Byte pcFlag;
    private List<String> mobileUris;
    private List<String> mobileUrls;
    private List<String> pcUris;
    private List<String> pcUrls;
    private List<AppEntryInfoDTO> appEntryInfos;
    private Byte independentConfigFlag;
    private List<Long> dependentAppIds;
    private List<String> dependentAppNames;
    private Byte supportThirdFlag;
    private String iconUri;
    private String iconUrl;
    private Byte systemAppFlag;
    private Byte defaultAppFlag;
    private List<ServiceModuleEntryDTO> serviceModuleEntryDtos;

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

    public Byte getClientHandlerType() {
        return clientHandlerType;
    }

    public void setClientHandlerType(Byte clientHandlerType) {
        this.clientHandlerType = clientHandlerType;
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

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
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

    public List<String> getMobileUris() {
        return mobileUris;
    }

    public void setMobileUris(List<String> mobileUris) {
        this.mobileUris = mobileUris;
    }

    public List<String> getMobileUrls() {
        return mobileUrls;
    }

    public void setMobileUrls(List<String> mobileUrls) {
        this.mobileUrls = mobileUrls;
    }

    public List<String> getPcUris() {
        return pcUris;
    }

    public void setPcUris(List<String> pcUris) {
        this.pcUris = pcUris;
    }

    public List<String> getPcUrls() {
        return pcUrls;
    }

    public void setPcUrls(List<String> pcUrls) {
        this.pcUrls = pcUrls;
    }

    public List<AppEntryInfoDTO> getAppEntryInfos() {
        return appEntryInfos;
    }

    public void setAppEntryInfos(List<AppEntryInfoDTO> appEntryInfos) {
        this.appEntryInfos = appEntryInfos;
    }

    public Byte getIndependentConfigFlag() {
        return independentConfigFlag;
    }

    public void setIndependentConfigFlag(Byte independentConfigFlag) {
        this.independentConfigFlag = independentConfigFlag;
    }

    public List<Long> getDependentAppIds() {
        return dependentAppIds;
    }

    public void setDependentAppIds(List<Long> dependentAppIds) {
        this.dependentAppIds = dependentAppIds;
    }

    public List<String> getDependentAppNames() {
        return dependentAppNames;
    }

    public void setDependentAppNames(List<String> dependentAppNames) {
        this.dependentAppNames = dependentAppNames;
    }

    public Byte getSupportThirdFlag() {
        return supportThirdFlag;
    }

    public void setSupportThirdFlag(Byte supportThirdFlag) {
        this.supportThirdFlag = supportThirdFlag;
    }

    public String getDisplayVersion() {
        return displayVersion;
    }

    public void setDisplayVersion(String displayVersion) {
        this.displayVersion = displayVersion;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getSystemAppFlag() {
        return systemAppFlag;
    }

    public void setSystemAppFlag(Byte systemAppFlag) {
        this.systemAppFlag = systemAppFlag;
    }

    public Byte getDefaultAppFlag() {
        return defaultAppFlag;
    }

    public void setDefaultAppFlag(Byte defaultAppFlag) {
        this.defaultAppFlag = defaultAppFlag;
    }

    public List<ServiceModuleEntryDTO> getServiceModuleEntryDtos() {
        return serviceModuleEntryDtos;
    }

    public void setServiceModuleEntryDtos(List<ServiceModuleEntryDTO> serviceModuleEntryDtos) {
        this.serviceModuleEntryDtos = serviceModuleEntryDtos;
    }
	
	public Byte getAccessControlType() {
        return accessControlType;
    }

    public void setAccessControlType(Byte accessControlType) {
        this.accessControlType = accessControlType;
    }

}
