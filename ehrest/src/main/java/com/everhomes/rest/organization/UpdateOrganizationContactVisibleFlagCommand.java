// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>contactToken：联系信息</li>
 * <li>visibleFlag：显示隐藏</li>
 * </ul>
 */
public class UpdateOrganizationContactVisibleFlagCommand {
	
    @NotNull
    private Long   organizationId;

	@NotNull
	private String contactToken;

	private Byte visibleFlag;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	public Byte getVisibleFlag() {
		return visibleFlag;
	}

	public void setVisibleFlag(Byte visibleFlag) {
		this.visibleFlag = visibleFlag;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
