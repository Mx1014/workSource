// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>dtos: dtos {@link com.everhomes.rest.acl.WebMenuDTO}</li>
 * </ul>
 */
public class UpdateMenuScopesByNamespaceCommand {

	private Integer namespaceId;

	@ItemType(WebMenuDTO.class)
	private List<WebMenuDTO> dtos;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

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
