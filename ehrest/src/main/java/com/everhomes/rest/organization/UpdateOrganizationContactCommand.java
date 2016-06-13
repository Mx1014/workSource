// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>contactName：联系名称</li>
 * <li>contactType：联系类型。0-手机号，1-邮箱 {@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：联系方式:手机号/邮箱</li>
 * </ul>
 */
public class UpdateOrganizationContactCommand {
	@NotNull
	private Long     id;

	private String   contactName;
	private Byte     contactType;
	@NotNull
	private String   contactToken;
	
	
	public UpdateOrganizationContactCommand() {
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
