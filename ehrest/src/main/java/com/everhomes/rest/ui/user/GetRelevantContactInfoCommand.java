package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户ID,和detailId二选一，都不传取当前用户</li>
 * <li>detailId: 联系人的档案id(当前用户不需要传)</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class GetRelevantContactInfoCommand {
	private Long userId;
	private Long detailId;
	private Long organizationId;

	public GetRelevantContactInfoCommand() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
