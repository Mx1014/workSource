package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.module.TerminalType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.util.StringHelper;

import java.io.Serializable;
import java.util.List;

/**
 * <ul>
 *     <li>id: 模块id</li>
 *     <li>moduleId: moduleId</li>
 *     <li>moduleName: moduleName</li>
 *     <li>entryName: entryName</li>
 *     <li>terminalType: 终端类型 {@link com.everhomes.rest.module.TerminalType}</li>
 *     <li>locationType: 位置类型 {@link com.everhomes.rest.module.ServiceModuleLocationType}</li>
 *     <li>sceneType: 场景类型 {@link com.everhomes.rest.module.ServiceModuleSceneType}</li>
 *     <li>appCategoryId: appCategoryId</li>
 *     <li>appCategoryName: 入口分类名称</li>
 *     <li>defaultOrder: 排序</li>
 *     <li>iconUri: iconUri</li>
 *     <li>iconUrl: iconUrl</li>
 * </ul>
 */
public class ServiceModuleEntryDTO {
	private Long id;
	private Long moduleId;
	private String moduleName;
	private String entryName;
	private Byte terminalType;
	private Byte locationType;
	private Byte sceneType;
	private Long appCategoryId;
	private String appCategoryName;
	private Integer defaultOrder;
	private String iconUri;
	private String iconUrl;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public Byte getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Byte terminalType) {
		this.terminalType = terminalType;
	}

	public Byte getLocationType() {
		return locationType;
	}

	public void setLocationType(Byte locationType) {
		this.locationType = locationType;
	}

	public Byte getSceneType() {
		return sceneType;
	}

	public void setSceneType(Byte sceneType) {
		this.sceneType = sceneType;
	}

	public Long getAppCategoryId() {
		return appCategoryId;
	}

	public void setAppCategoryId(Long appCategoryId) {
		this.appCategoryId = appCategoryId;
	}

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
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

	public String getAppCategoryName() {
		return appCategoryName;
	}

	public void setAppCategoryName(String appCategoryName) {
		this.appCategoryName = appCategoryName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}