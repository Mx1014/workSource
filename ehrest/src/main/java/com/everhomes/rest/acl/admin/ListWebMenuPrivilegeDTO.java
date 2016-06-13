package com.everhomes.rest.acl.admin;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.WebMenuPrivilegeDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>moduleId: 模块id</li>
 * <li>moduleName: 模块名称</li>
 * <li>dtos: 权限集合，参考{@link com.everhomes.rest.acl.WebMenuPrivilegeDTO}</li>
 * </ul>
 */
public class ListWebMenuPrivilegeDTO {
	
	private Long moduleId;
	
	private String moduleName;

	@ItemType(WebMenuPrivilegeDTO.class)
	private List<WebMenuPrivilegeDTO> dtos;
	
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

	public List<WebMenuPrivilegeDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<WebMenuPrivilegeDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
