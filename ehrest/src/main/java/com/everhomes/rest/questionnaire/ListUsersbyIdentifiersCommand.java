// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>identifiers: 手机号数组, 参考{@link java.lang.String}</li>
 * </ul>
 */
public class ListUsersbyIdentifiersCommand {

	private Integer namespaceId;

	@ItemType(String.class)
	private List<String> identifiers;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
