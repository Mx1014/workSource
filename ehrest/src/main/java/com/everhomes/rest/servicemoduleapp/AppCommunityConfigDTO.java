package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>organizationAppId: 安装id</li>
 *     <li>appOriginId: appOriginId</li>
 *     <li>communityId: 园区Id</li>
 *     <li>visibilityFlag: 是否可见{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>displayName: 应用入口名称</li>
 *     <li>serviceModuleAppName: 所属应用名称</li>
 *     <li>iconUrl: 所属应用icon</li>
 *     <li>appNo: 应用详情profile编号</li>
 *     <li>moduleId: moduleId</li>
 *     <li>recommendFlag: 是否推荐</li>
 * </ul>
 */
public class AppCommunityConfigDTO {
	private Long id;
	private Long organizationAppId;
	private Long appOriginId;
	private Long communityId;
	private Byte visibilityFlag;
	private String displayName;
	private String serviceModuleAppName;
	private String iconUrl;
	private String appNo;
	private Long moduleId;
	private Byte recommendFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganizationAppId() {
		return organizationAppId;
	}

	public void setOrganizationAppId(Long organizationAppId) {
		this.organizationAppId = organizationAppId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Byte getVisibilityFlag() {
		return visibilityFlag;
	}

	public void setVisibilityFlag(Byte visibilityFlag) {
		this.visibilityFlag = visibilityFlag;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Long getAppOriginId() {
		return appOriginId;
	}

	public void setAppOriginId(Long appOriginId) {
		this.appOriginId = appOriginId;
	}

	public String getServiceModuleAppName() {
		return serviceModuleAppName;
	}

	public void setServiceModuleAppName(String serviceModuleAppName) {
		this.serviceModuleAppName = serviceModuleAppName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Byte getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(Byte recommendFlag) {
		this.recommendFlag = recommendFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
