// @formatter:off
package com.everhomes.rest.module;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor：锚点 </li>
 * <li>dtos：列表</li>
 * </ul>
 */
public class ListServiceModulesResponse {

	private Long nextPageAnchor;

	@ItemType(ServiceModuleDTO.class)
	private List<ServiceModuleDTO> dtos;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ServiceModuleDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ServiceModuleDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
