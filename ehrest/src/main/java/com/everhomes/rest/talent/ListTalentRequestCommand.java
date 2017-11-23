// @formatter:off
package com.everhomes.rest.talent;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</li>
 * <li>ownerId: 所属id</li>
 * <li>organizationId: 管理公司id</li>
 * <li>keyword: 关键词</li>
 * <li>beginTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListTalentRequestCommand {

	private String ownerType;

	private Long ownerId;

	private Long organizationId;

	private String keyword;

	private Long beginTime;

	private Long endTime;

	private Long pageAnchor;

	private Integer pageSize;

	public ListTalentRequestCommand() {

	}

	public ListTalentRequestCommand(String ownerType, Long ownerId, Long organizationId, String keyword, Long beginTime, Long endTime, Long pageAnchor, Integer pageSize) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.organizationId = organizationId;
		this.keyword = keyword;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
	}

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
