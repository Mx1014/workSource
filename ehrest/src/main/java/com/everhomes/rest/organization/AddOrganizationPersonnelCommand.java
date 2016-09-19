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
 * <li>contactName：成员名称</li>
 * <li>contactToken：联系信息</li>
 * <li>gender：性别</li>
 * <li>employeeNo：工号</li>
 * <li>contactDescription：描述</li>
 * <li>childOrganizationIds：添加到多部门或者多群组</li>
 * </ul>
 */
public class AddOrganizationPersonnelCommand {
	
    @NotNull
    private Long   organizationId;
   
	private String targetType;
	
	private Long   targetId;
	
	private String contactName;
	
	private String contactToken;
	
	private Byte gender;
	
	private String employeeNo;
	
	@ItemType(Long.class)
	private List<Long> childOrganizationIds;
	

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

	public String getContactName() {
		return contactName;
	}


	public void setContactName(String contactName) {
		this.contactName = contactName;
	}


	public String getContactToken() {
		return contactToken;
	}


	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
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


	public List<Long> getChildOrganizationIds() {
		return childOrganizationIds;
	}


	public void setChildOrganizationIds(List<Long> childOrganizationIds) {
		this.childOrganizationIds = childOrganizationIds;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
