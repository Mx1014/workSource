// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>organizationId：机构id</li>
 * <li>communityIds：小区id列表</li>
 * <li>message：消息内容</li>
 * </ul>
 */
public class SendOrganizationMessageCommand {
	@NotNull
	private Long organizationId;
	@NotNull
	@ItemType(Long.class)
	private List<Long> communityIds;

	private String message;
	
	public Long getOrganizationId() {
		return organizationId;
	}



	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}



	public List<Long> getCommunityIds() {
		return communityIds;
	}



	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
