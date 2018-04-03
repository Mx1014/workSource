// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.acl.WebMenuDTO}</li>
 * </ul>
 */
public class GetTreeWebMenusByNamespaceResponse {

	@ItemType(WebMenuDTO.class)
	private List<WebMenuDTO> dtos;

	public List<WebMenuDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<WebMenuDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
