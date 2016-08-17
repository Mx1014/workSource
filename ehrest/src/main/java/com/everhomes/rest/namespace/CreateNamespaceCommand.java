// @formatter:off
package com.everhomes.rest.namespace;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name：命名空间名称</li>
 * <li>communityType：小区类型，参考{@link com.everhomes.rest.namespace.NamespaceCommunityType}</li>
 * </ul>
 */
public class CreateNamespaceCommand {
	private String name;
	private String communityType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommunityType() {
		return communityType;
	}

	public void setCommunityType(String communityType) {
		this.communityType = communityType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
