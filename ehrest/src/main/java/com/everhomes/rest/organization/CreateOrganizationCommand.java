// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>parentId：父机构id。没有填0</li>
 * <li>name：名称</li>
 * <li>path：路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>level：当前层级。没有填0</li>
 * <li>addressId：组织地址</li>
 * <li>OrganizationType：组织类型：参考{@link com.everhomes.rest.organization.OrganizationType}</li>
 * </ul>
 */
public class CreateOrganizationCommand {
	private Long    parentId;
	@NotNull
	private String  name;
	private String  path;
	private Integer level;
	private Long    addressId;
	private String   OrganizationType;
	
	private String groupType;
	
	private Long roleId;
	
	public CreateOrganizationCommand() {
    }
	
	
	public Long getParentId() {
		return parentId;
	}


	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public Integer getLevel() {
		return level;
	}


	public void setLevel(Integer level) {
		this.level = level;
	}


	public Long getAddressId() {
		return addressId;
	}


	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}


	public String getOrganizationType() {
		return OrganizationType;
	}


	public void setOrganizationType(String organizationType) {
		OrganizationType = organizationType;
	}


	public String getGroupType() {
		return groupType;
	}


	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}


	public Long getRoleId() {
		return roleId;
	}


	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
