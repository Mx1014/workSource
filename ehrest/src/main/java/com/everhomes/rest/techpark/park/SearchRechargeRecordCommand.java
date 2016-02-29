package com.everhomes.rest.techpark.park;

/**
 * <ul>
 *  <li>ownerName: 车主姓名</li>
 *  <li>rechargePhone: 充值人人手机号</li>
 *  <li>plateNumber: 车牌号</li>
 *  <li>communityId: 园区id</li>
 *  <li>startTime: 充值开始时间</li>
 *  <li>endTime: 充值结束时间</li>
 *  <li>rechargeStatus: 充值状态</li>
 * </ul>
 *
 */
public class SearchRechargeRecordCommand {
	
	private String ownerName;
	
	private String rechargePhone;
	
	private String plateNumber;
	
	private Long communityId;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private Long startTime;
	
	private Long endTime;
	
	private Byte rechargeStatus;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getRechargePhone() {
		return rechargePhone;
	}

	public void setRechargePhone(String rechargePhone) {
		this.rechargePhone = rechargePhone;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
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

	public Byte getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(Byte rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}
	
}
