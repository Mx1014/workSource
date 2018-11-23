package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * 
 * <ul>
 * <li>id : 组织id</li>
 * <li>name : 组织名称</li>
 * <li>organizationType : 组织类型</li>
 * <li>groupType：机构类别，需要首先用该值来判断是否是一个机构（其还有可能是部门、群组等），在该值为机构的情况下，OrganizationType才有意义，{@link com.everhomes.rest.organization.OrganizationGroupType}</li>
 * <li>directlyEnterpriseId：如果本身是机构，则此值无效；如果本身是部门、群组（可能多层），则其层次结构上最近的一个直属机构</li>
 * 物业或业委 需要附加字段 :
 * <li>communityId : 小区id</li>
 * <li>communityName : 小区name</li>
 * <li>userIsManage : 是否为管理员</li>
 *	</ul>
 */
public class OrganizationSimpleDTO {

	private Long id;

	private String name;

	private String organizationType;
	
	private Long communityId;
	
	private String communityName;
	
	private Long directlyEnterpriseId;
	
	private String groupType;

	private String contactName;

	private String contactToken;

	private String displayName;

	private Byte userIsManage;

    public Byte getUserIsManage() {
        return userIsManage;
    }

    public void setUserIsManage(Byte userIsManage) {
        this.userIsManage = userIsManage;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}
	
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

	public Long getDirectlyEnterpriseId() {
		return directlyEnterpriseId;
	}

	public void setDirectlyEnterpriseId(Long directlyEnterpriseId) {
		this.directlyEnterpriseId = directlyEnterpriseId;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	




}
