// @formatter:off
package com.everhomes.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 小区id</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.use.PmMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>pmGroup：物业角色类型 参考{@link com.everhomes.pm.PmGroup}</li>
 * <li>contactName：成员名称</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：成员标识</li>
 * <li>contactDescription：描述</li>
 * </ul>
 */
public class CreateOrganizationMemberCommand {
    @NotNull
    private Long   organizationId;
   
	private String targetType;
    @NotNull
	private Long   targetId;

	private String pmGroup;
	private String contactName;
	private Byte   contactType;
	private String contactToken;
	private String contactDescription;
	
	public CreateOrganizationMemberCommand() {
    }
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
	public String getPmGroup() {
		return pmGroup;
	}
	public void setPmGroup(String pmGroup) {
		this.pmGroup = pmGroup;
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
	public String getContactDescription() {
		return contactDescription;
	}
	public void setContactDescription(String contactDescription) {
		this.contactDescription = contactDescription;
	}
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
