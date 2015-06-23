// @formatter:off
package com.everhomes.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>organizationId：政府id</li>
 * <li>memberUid：用户id</li>
 * <li>memberGroup：成员类型。参考 {@link com.everhomes.organization.OrganizationGroup}</li>
 * </ul>
 */
public class OrganizationMemberDTO {
	@NotNull
	private Long   organizationId;
	@NotNull
	private Long   memberUid;
	@NotNull
	private String memberGroup;
	
	public OrganizationMemberDTO() {
    }
	
	
	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public Long getMemberUid() {
		return memberUid;
	}


	public void setMemberUid(Long memberUid) {
		this.memberUid = memberUid;
	}


	public String getMemberGroup() {
		return memberGroup;
	}


	public void setMemberGroup(String memberGroup) {
		this.memberGroup = memberGroup;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
