package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 联系人的档案id(当前用户不需要传)</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class GetRelevantContactInfoCommand {

	private Long detailId;

	private Long organizationId;

	public GetRelevantContactInfoCommand() {
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
