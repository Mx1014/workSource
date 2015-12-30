// @formatter:off
package com.everhomes.rest.organization;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>organizationId：组织id。</li>
 * <li>contactName：联系名称</li>
 * <li>contactType：联系类型。0-手机号，1-邮箱 {@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：联系方式:手机号/邮箱</li>
 * <li>creatorUid：创建者uId</li>
 * <li>createTime：创建时间</li>
 * </ul>
 */
public class OrganizationContactDTO {
	private Long     id;
	private Long     organizationId;
	private String   contactName;
	private Byte     contactType;
	private String   contactToken;
	private Long     creatorUid;
	private Timestamp createTime;
	
	public OrganizationContactDTO() {
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
