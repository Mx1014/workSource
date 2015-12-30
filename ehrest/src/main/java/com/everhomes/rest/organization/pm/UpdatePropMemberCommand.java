// @formatter:off
package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>communityId: 小区id</li>
 * <li>targetType：成员是否注册{@link com.everhomes.use.PmMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>pmGroup：物业角色类型 参考{@link com.everhomes.PmMemberGroup.PmGroup}</li>
 * <li>contactName：成员名称</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：成员标识</li>
 * <li>contactDescription：描述</li>
 * </ul>
 */
public class UpdatePropMemberCommand {
    @NotNull
    private Long   id;
    private Long   communityId;
	private String targetType;
	private Long   targetId;
	private String pmGroup;
	private String contactName;
	private Byte   contactType;
	private String contactToken;
	private String contactDescription;

	
	public UpdatePropMemberCommand() {
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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
