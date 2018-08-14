package com.everhomes.rest.module;

import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.rest.servicemoduleapp.AppCommunityConfigDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: 应用分类id</li>
 *     <li>name: 应用分类名称</li>
 *     <li>parentId: 父级id</li>
 *     <li>locationType: 分类位置，参考{@link ServiceModuleLocationType}</li>
 *     <li>appType: 应用类型0-OA，1-园区，2-服务应用 {@link ServiceModuleAppType}</li>
 *     <li>defaultOrder: defaultOrder</li>
 *     <li>leafFlag: 是否为叶子节点，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>dtos: 下级分类，参考{@link AppCategoryDTO}</li>
 *     <li>appDtos: appDtos，广场应用，参考{@link AppDTO}</li>
 *     <li>appCommunityConfigDtos: web端项目广场{@link AppCommunityConfigDTO}</li>
 *     <li>menuDtos: web后台菜单{@link WebMenuDTO}</li>
 * </ul>
 */
public class AppCategoryDTO {


	private Long id;

	private String name;

	private Long parentId;

	private Byte locationType;

	private Byte appType;

	private Long defaultOrder;

	private Byte leafFlag;

	private List<AppCategoryDTO> dtos;

	private List<AppDTO> appDtos;

	private List<AppCommunityConfigDTO> appCommunityConfigDtos;

	private List<WebMenuDTO> menuDtos;

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

	public Byte getLocationType() {
		return locationType;
	}

	public void setLocationType(Byte locationType) {
		this.locationType = locationType;
	}

	public Byte getAppType() {
		return appType;
	}

	public void setAppType(Byte appType) {
		this.appType = appType;
	}

	public Long getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Long defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public List<AppCategoryDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<AppCategoryDTO> dtos) {
		this.dtos = dtos;
	}

	public Byte getLeafFlag() {
		return leafFlag;
	}

	public void setLeafFlag(Byte leafFlag) {
		this.leafFlag = leafFlag;
	}


	public List<AppDTO> getAppDtos() {
		return appDtos;
	}

	public void setAppDtos(List<AppDTO> appDtos) {
		this.appDtos = appDtos;
	}

	public List<AppCommunityConfigDTO> getAppCommunityConfigDtos() {
		return appCommunityConfigDtos;
	}

	public void setAppCommunityConfigDtos(List<AppCommunityConfigDTO> appCommunityConfigDtos) {
		this.appCommunityConfigDtos = appCommunityConfigDtos;
	}

	public List<WebMenuDTO> getMenuDtos() {
		return menuDtos;
	}

	public void setMenuDtos(List<WebMenuDTO> menuDtos) {
		this.menuDtos = menuDtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}