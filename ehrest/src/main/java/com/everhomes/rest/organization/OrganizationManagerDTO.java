package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>memberId: 通讯录id</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.pm.PmMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>contactName：成员名称</li>
 * <li>contactType：成员类型：{@link com.everhomes.rest.user.IdentifierType}</li>
 * <li>contactToken：成员标识</li>
 * </ul>
 *
 */
public class OrganizationManagerDTO {
	
	private Long memberId;

	private String targetType;

	private Long   targetId;

	private String contactName;

	private Byte   contactType;

	private String contactToken;


	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
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
