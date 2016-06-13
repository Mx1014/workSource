// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：组织id。</li>
 * <li>contactName：联系名称</li>
 * <li>contactType：联系类型。0-手机号，1-邮箱 {@link com.everhomes.rest.user.IdentifierType}</li>
 * <li>contactToken：联系方式:手机号/邮箱</li>
 * </ul>
 */
public class CreateOrganizationContactCommand {
	private Long     organizationId;
	private String   contactName;
	private Byte     contactType;
	private String   contactToken;
	
	public CreateOrganizationContactCommand() {
    }
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Byte getContactType() {
		return contactType;
	}

	public void setContactType(Byte contactType) {
		this.contactType = contactType;
	}

	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
