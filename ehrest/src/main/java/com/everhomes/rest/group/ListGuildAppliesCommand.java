// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>groupId: groupId,选填</li>
 *     <li>applicantUid: 创建者id，选填</li>
 * </ul>
 */
public class ListGuildAppliesCommand {

	Integer namespaceId;
	Long groupId;
	Long applicantUid;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getApplicantUid() {
		return applicantUid;
	}

	public void setApplicantUid(Long applicantUid) {
		this.applicantUid = applicantUid;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
