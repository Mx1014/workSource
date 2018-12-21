// @formatter:off
package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>tagOnlyFlag: 是否只查标签 0-还包括全部/我发布的等等 1-只查标签</li>
 * </ul>
 */
public class ListTagsCommand {
	private Long organizationId;
	private Byte tagOnlyFlag;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Byte getTagOnlyFlag() {
		return tagOnlyFlag;
	}

	public void setTagOnlyFlag(Byte tagOnlyFlag) {
		this.tagOnlyFlag = tagOnlyFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
