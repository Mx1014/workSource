// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>contactTokens：联系信息</li>
 * <li>visibleFlag：隐藏显示</li>
 * </ul>
 */
public class BatchUpdateOrganizationContactVisibleFlagCommand {
	
    @NotNull
    private Long   organizationId;

	@ItemType(String.class)
	private List<String> contactTokens;

	private Byte visibleFlag;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<String> getContactTokens() {
		return contactTokens;
	}

	public void setContactTokens(List<String> contactTokens) {
		this.contactTokens = contactTokens;
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
