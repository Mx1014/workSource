package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 查询场所
 * <li>ownerType：可见范围类型：“community”</li>
 * <li>ownerId：可见范围的id</li>
 * <li>communityId：所属园区id</li>
 * <li>resourceTypeId: 广场图标id</li>
 * <li>keyword: 关键字</li>
 * <li>anchor: 锚点</li>
 * <li>pageSize: 每页个数</li>
 * <li>status: 状态码</li>
 * <li>startTime: 开始筛选时间</li>
 * <li>rentalType: 预订模式</li>
 * <li>endTime: 结束筛选时间</li>
 * <li>startTimeAmOrPm: 开始时间上午还是下午还是晚上(半天时用) {@link AmorpmFlag}</li>
 * <li>endTimeAmOrPm: 结束时间上午还是下午还是晚上(半天时用) {@link AmorpmFlag}</li>
 * </ul>
 */
public class FindRentalSitesCommand {
	private String ownerType;
	private Long ownerId; 
	private Long communityId;
	private String resourceType;
	private Long resourceTypeId;
	private String keyword;
	private Byte rentalType;
	private Long startTime;
	private Long endTime;
	private Byte startTimeAmOrPm;
	private Byte endTimeAmOrPm;

	private String sceneType;
	private String sceneToken;

	private Long anchor;
	private Integer pageSize;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	public Long getResourceTypeId() {
		return resourceTypeId;
	}
	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Long getAnchor() {
		return anchor;
	}

	public void setAnchor(Long anchor) {
		this.anchor = anchor;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getSceneType() {
		return sceneType;
	}

	public void setSceneType(String sceneType) {
		this.sceneType = sceneType;
	}

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Byte getStartTimeAmOrPm() {
		return startTimeAmOrPm;
	}

	public void setStartTimeAmOrPm(Byte startTimeAmOrPm) {
		this.startTimeAmOrPm = startTimeAmOrPm;
	}

	public Byte getEndTimeAmOrPm() {
		return endTimeAmOrPm;
	}

	public void setEndTimeAmOrPm(Byte endTimeAmOrPm) {
		this.endTimeAmOrPm = endTimeAmOrPm;
	}

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}
}
