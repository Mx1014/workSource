package com.everhomes.rest.organization;


import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>id：organizationid</li>
 * <li>parentId：父机构id。没有填科技园的organizationid</li>
 * <li>address：地址</li>
 * <li>name：名称</li>
 * <li>addManagerMemberIds：添加的经理通讯录id</li>
 * <li>delManagerMemberIds：删除的经理通讯录id</li>
 * <li>jobPositionIds：通用岗位</li>
 * <li>size：职级大小 </li>
 * <li>communityId：入住园区</li>
 * <li>scopeType：入住的节点范围类型{@link com.everhomes.rest.organization.OrganizationCommunityScopeType}</li>
 * </ul>
 */
public class UpdateOrganizationsCommand {
	
	private Long id;

	private String name;
	
	private String address;
	
	private Long parentId;
	
	private Byte naviFlag;

	private Integer size;

	@ItemType(Long.class)
	private List<Long> addManagerMemberIds;

	@ItemType(Long.class)
	private List<Long> delManagerMemberIds;

	@ItemType(Long.class)
	private List<Long> jobPositionIds;

	@ItemType(Long.class)
	private List<Long> addMemberIds;

	@ItemType(Long.class)
	private List<Long> delMemberIds;

	private Long communityId;

	private String scopeType;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Byte getNaviFlag() {
		return naviFlag;
	}

	public void setNaviFlag(Byte naviFlag) {
		this.naviFlag = naviFlag;
	}

	public List<Long> getJobPositionIds() {
		return jobPositionIds;
	}

	public void setJobPositionIds(List<Long> jobPositionIds) {
		this.jobPositionIds = jobPositionIds;
	}

	public List<Long> getAddManagerMemberIds() {
		return addManagerMemberIds;
	}

	public void setAddManagerMemberIds(List<Long> addManagerMemberIds) {
		this.addManagerMemberIds = addManagerMemberIds;
	}

	public List<Long> getDelManagerMemberIds() {
		return delManagerMemberIds;
	}

	public void setDelManagerMemberIds(List<Long> delManagerMemberIds) {
		this.delManagerMemberIds = delManagerMemberIds;
	}

	public List<Long> getAddMemberIds() {
		return addMemberIds;
	}

	public void setAddMemberIds(List<Long> addMemberIds) {
		this.addMemberIds = addMemberIds;
	}

	public List<Long> getDelMemberIds() {
		return delMemberIds;
	}

	public void setDelMemberIds(List<Long> delMemberIds) {
		this.delMemberIds = delMemberIds;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}
}
