package com.everhomes.rest.acl;

import com.everhomes.rest.module.TerminalType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 模块id</li>
 *     <li>order: order</li>
 *     <li>moduleId: moduleId</li>
 *     <li>moduleName: moduleName</li>
 *     <li>entryName: entryName</li>
 *     <li>appTypeName: appTypeName</li>
 *     <li>terminalType: 终端类型 {@link TerminalType}</li>
 *     <li>terminalTypeName: terminalTypeName</li>
 *     <li>locationType: 位置类型 {@link com.everhomes.rest.module.ServiceModuleLocationType}</li>
 *     <li>locationTypeName: locationTypeName</li>
 *     <li>sceneType: 场景类型 {@link com.everhomes.rest.module.ServiceModuleSceneType}</li>
 *     <li>sceneTypeName: sceneTypeName</li>
 *     <li>appCategoryId: appCategoryId</li>
 *     <li>appCategoryName: appCategoryName</li>
 *     <li>defaultOrder: defaultOrder</li>
 * </ul>
 */
public class ServiceModuleEntryExportDTO {
	private Long id;
	private Integer order;
	private Long moduleId;
	private String moduleName;
	private String entryName;
	private String appTypeName;
	private Byte terminalType;
	private String terminalTypeName;
	private Byte locationType;
	private String locationTypeName;
	private Byte sceneType;
	private String sceneTypeName;
	private Long appCategoryId;
	private String appCategoryName;
	private Integer defaultOrder;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
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

	public String getAppTypeName() {
		return appTypeName;
	}

	public void setAppTypeName(String appTypeName) {
		this.appTypeName = appTypeName;
	}

	public String getTerminalTypeName() {
		return terminalTypeName;
	}

	public void setTerminalTypeName(String terminalTypeName) {
		this.terminalTypeName = terminalTypeName;
	}

	public String getLocationTypeName() {
		return locationTypeName;
	}

	public void setLocationTypeName(String locationTypeName) {
		this.locationTypeName = locationTypeName;
	}

	public String getSceneTypeName() {
		return sceneTypeName;
	}

	public void setSceneTypeName(String sceneTypeName) {
		this.sceneTypeName = sceneTypeName;
	}

	public String getAppCategoryName() {
		return appCategoryName;
	}

	public void setAppCategoryName(String appCategoryName) {
		this.appCategoryName = appCategoryName;
	}

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}