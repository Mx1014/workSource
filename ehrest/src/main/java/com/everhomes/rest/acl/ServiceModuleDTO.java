package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <ul>
 *     <li>id: 模块id</li>
 *     <li>name: 模块名称</li>
 *     <li>vType: 值类型 0业务模块，1权限</li>
 *     <li>parentId: 父级id</li>
 *     <li>path: 层次关系</li>
 *     <li>level: 级别</li>
 *     <li>serviceModules: 子业务模块 {@link com.everhomes.rest.acl.ServiceModuleDTO}</li>
 *     <li>serviceModuleApps: 模块关联应用 {@link com.everhomes.rest.acl.ServiceModuleDTO}</li>
 *     <li>type: type</li>
 *     <li>description: 描述</li>
 *     <li>updateTime: 更新时间</li>
 *     <li>createTime: 创建时间</li>
 *     <li>operatorUid: operatorUid</li>
 *     <li>operatorUName: 操作人</li>
 *     <li>instanceConfig: 参数，比如第三方链接值就是{"url":"http......."}</li>
 *     <li>multipleFlag: multipleFlag</li>
 *     <li>moduleControlType: moduleControlType</li>
 *     <li>systemAppFlag: 是否为系统应用，例如“系统管理员”是系统应用。0-no, 1-yes。参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>appType: 应用类型0-OA，1-园区，2-服务应用 {@link com.everhomes.rest.module.ServiceModuleAppType}</li>
 *     <li>category: 模块分类，参考{@link com.everhomes.rest.acl.ServiceModuleCategory}</li>
 *     <li>accessControlType: 权限控制类型 参考{@link com.everhomes.rest.module.AccessControlType}</li>
 *     <li>iconUri: iconUri</li>
 *     <li>iconUrl: iconUrl</li>
 * </ul>
 */
public class ServiceModuleDTO implements Serializable {
	private Long id;

	private String name;

	private Byte vType;

	private Long parentId;

	private String path;

	private Integer level;

	@ItemType(ServiceModuleDTO.class)
	private List<ServiceModuleDTO> serviceModules;

	@ItemType(ServiceModuleDTO.class)
	private List<ServiceModuleAppDTO> serviceModuleApps;

	private Byte type;

	private String description;

	private Long updateTime;

	private Long createTime;

	private Long operatorUid;

	private String operatorUName;

	private String instanceConfig;

	private Byte multipleFlag;

	private String moduleControlType;

	private String systemAppFlag;

	private Byte appType;

	private String category;

	private Byte accessControlType;

	private String iconUri;

	private String iconUrl;

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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Byte getvType() {
		return vType;
	}

	public void setvType(Byte vType) {
		this.vType = vType;
	}

	public List<ServiceModuleDTO> getServiceModules() {
		return serviceModules;
	}

	public void setServiceModules(List<ServiceModuleDTO> serviceModules) {
		this.serviceModules = serviceModules;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public String getOperatorUName() {
		return operatorUName;
	}

	public void setOperatorUName(String operatorUName) {
		this.operatorUName = operatorUName;
	}

	public String getInstanceConfig() {
		return instanceConfig;
	}

	public void setInstanceConfig(String instanceConfig) {
		this.instanceConfig = instanceConfig;
	}

	public Byte getMultipleFlag() {
		return multipleFlag;
	}

	public void setMultipleFlag(Byte multipleFlag) {
		this.multipleFlag = multipleFlag;
	}

	public List<ServiceModuleAppDTO> getServiceModuleApps() {
		return serviceModuleApps;
	}

	public void setServiceModuleApps(List<ServiceModuleAppDTO> serviceModuleApps) {
		this.serviceModuleApps = serviceModuleApps;
	}

	public String getModuleControlType() {
		return moduleControlType;
	}

	public void setModuleControlType(String moduleControlType) {
		this.moduleControlType = moduleControlType;
	}

	public String getSystemAppFlag() {
		return systemAppFlag;
	}

	public void setSystemAppFlag(String systemAppFlag) {
		this.systemAppFlag = systemAppFlag;
	}

	public Byte getAppType() {
		return appType;
	}

	public void setAppType(Byte appType) {
		this.appType = appType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Byte getAccessControlType() {
		return accessControlType;
	}

	public void setAccessControlType(Byte accessControlType) {
		this.accessControlType = accessControlType;
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
}