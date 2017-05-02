// @formatter:off
package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>parentId：父机构id。没有填0</li>
 * <li>name：名称</li>
 * <li>address：地址</li>
 * <li>groupType：机构类型{@link com.everhomes.rest.organization.OrganizationGroupType}</li>
 * <li>naviFlag：是否展示在导航{@link com.everhomes.rest.organization.OrganizationNaviFlag}</li>
 * <li>addManagerMemberIds：添加的经理通讯录id</li>
 * <li>delManagerMemberIds：删除的经理通讯录id</li>
 * <li>jobPositionIds：通用岗位</li>
 * <li>size：职级大小 </li>
 * <li>addMemberIds：添加的人员</li>
 * <li>delMemberIds：删除的人员</li>
 * <li>communityId：入住园区</li>
 * </ul>
 */
public class CreateOrganizationCommand {
	private Long    parentId;
	@NotNull
	private String  name;
	
	private String address;
	
	private String groupType;
	
	private Byte naviFlag;

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

	private Integer size;

	private Long communityId;


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


	public String getGroupType() {
		return groupType;
	}


	public void setGroupType(String groupType) {
		this.groupType = groupType;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
