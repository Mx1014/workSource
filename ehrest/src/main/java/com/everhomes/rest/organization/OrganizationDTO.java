// @formatter:off
package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>parentId：父机构id。没有填0</li>
 * <li>name：名称</li>
 * <li>path：路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>level：当前层级。没有填0</li>
 * <li>addressId：地址</li>
 * <li>OrganizationType：组织类型：参考{@link com.everhomes.rest.organization.OrganizationType}</li>
 * <li>status：状态：参考{@link com.everhomes.rest.organization.OrganizationStatus}</li>
 * <li>memberStatus：成员状态：参考{@link com.everhomes.rest.organization.OrganizationMemberStatus}</li>
 * <li>description：组织描述</li>
 * <li>childrens：子集参考{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>communityId：i小区id</li>
 * <li>communityName：小区名称</li>
 * <li>
 * </ul>
 */
public class OrganizationDTO {
	
	private Long    id;
    private Long    parentId;
    private String parentName;
	private String  name;
	private String  path;
	private Integer level;
	private Long addressId;
	private String OrganizationType;
	private Byte    status;
	private Byte memberStatus;
	private String description;
	private String address;
	private String groupType;
	private Long directlyEnterpriseId;
	
    private String avatarUri;
    private String avatarUrl;
    private String contact;

    private String displayName;
	
	//expand 
	private Long communityId;
	private String communityName;
	
	@ItemType(OrganizationDTO.class)
	private List<OrganizationDTO> childrens;
	
	@ItemType(RoleDTO.class)
	private List<RoleDTO> roles;
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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<OrganizationDTO> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<OrganizationDTO> childrens) {
		this.childrens = childrens;
	}

	
	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}



	public Long getDirectlyEnterpriseId() {
		return directlyEnterpriseId;
	}


	public void setDirectlyEnterpriseId(Long directlyEnterpriseId) {
		this.directlyEnterpriseId = directlyEnterpriseId;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
