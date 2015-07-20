// @formatter:off
package com.everhomes.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>parentId：父机构id。没有填0</li>
 * <li>name：名称</li>
 * <li>path：路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>level：当前层级。没有填0</li>
 * <li>addressId：地址</li>
 * <li>OrganizationType：组织类型：参考{@link com.everhomes.organization.OrganizationType}</li>
 * <li>status：状态：参考{@link com.everhomes.organization.OrganizationStatus}</li>
 * <li>memberStatus：成员状态：参考{@link com.everhomes.organization.OrganizationMemberStatus}</li>
 * <li>description：组织描述</li>
 * 
 * <li>communityId：i小区id</li>
 * <li>communityName：小区名称</li>
 * <li>
 * </ul>
 */
public class OrganizationDTO {
	
	private Long    id;
    private Long    parentId;
	private String  name;
	private String  path;
	private Integer level;
	private Long addressId;
	private String OrganizationType;
	private Byte    status;
	private Byte memberStatus;
	private String description;
	
	//expand 
	private Long communityId;
	private String communityName;
	
	
	
	public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	public String getCommunityName() {
		return communityName;
	}


	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public OrganizationDTO() {
    }
	
	
	public java.lang.Long getParentId() {
		return parentId;
	}


	public void setParentId(java.lang.Long parentId) {
		this.parentId = parentId;
	}


	public java.lang.String getName() {
		return name;
	}


	public void setName(java.lang.String name) {
		this.name = name;
	}


	public java.lang.String getPath() {
		return path;
	}


	public void setPath(java.lang.String path) {
		this.path = path;
	}


	public java.lang.Integer getLevel() {
		return level;
	}


	public void setLevel(java.lang.Integer level) {
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


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public Byte getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Byte memberStatus) {
		this.memberStatus = memberStatus;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
