package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerType: 拥有者类型：现在是comunity</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>categoryId: 服务联盟大类id</li>
 *  <li>startDay: 开始时间</li>
 *  <li>endDay：结束时间</li>
 *  <li>creatorName：用户姓名</li>
 *  <li>creatorOrganization：机构名称</li>
 * </ul>
 */
public class SearchRequestInfoCommand {

	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private Long categoryId;
	
	private Long startDay;
	
	private Long endDay;
	
	private String creatorName;
	
	private String creatorOrganization;

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCreatorOrganization() {
		return creatorOrganization;
	}

	public void setCreatorOrganization(String creatorOrganization) {
		this.creatorOrganization = creatorOrganization;
	}

	public Long getStartDay() {
		return startDay;
	}

	public void setStartDay(Long startDay) {
		this.startDay = startDay;
	}

	public Long getEndDay() {
		return endDay;
	}

	public void setEndDay(Long endDay) {
		this.endDay = endDay;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
