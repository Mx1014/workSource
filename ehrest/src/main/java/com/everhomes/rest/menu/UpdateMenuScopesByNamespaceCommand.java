// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>jsonDtos: jsonDtos</li>
 * </ul>
 */
public class UpdateMenuScopesByNamespaceCommand {

	private Integer namespaceId;

	private String jsonDtos;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getJsonDtos() {
		return jsonDtos;
	}

	public void setJsonDtos(String jsonDtos) {
		this.jsonDtos = jsonDtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
