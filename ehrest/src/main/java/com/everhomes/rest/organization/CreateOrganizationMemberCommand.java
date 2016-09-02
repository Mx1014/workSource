// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.OrganizationMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>memberGroup：组织角色类型 参考{@link com.everhomes.rest.organization.OrganizationMemberGroupType}</li>
 * <li>contactName：成员名称</li>
 * <li>contactType：联系类型：{@link com.everhomes.rest.user.IdentifierType}</li>
 * <li>contactToken：联系信息</li>
 * <li>gender：性别</li>
 * <li>employeeNo：工号</li>
 * <li>contactDescription：描述</li>
 * <li>必填：organizationId，targetType。注册用户：targetId，memberGroup。未注册：memberGroup，contactType，contactToken，contactName</li>
 * </ul>
 */
public class CreateOrganizationMemberCommand {
    @NotNull
    private Long   organizationId;
   
	private String targetType;
	private Long   targetId;

	private String memberGroup;
	private String contactName;
	private Byte   contactType;
	private String contactToken;
	private String contactDescription;
	private Byte gender;
	private String employeeNo;
	
	private Long groupId;
	
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
	public String getMemberGroup() {
		return memberGroup;
	}

	public void setMemberGroup(String memberGroup) {
		this.memberGroup = memberGroup;
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

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}


	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
