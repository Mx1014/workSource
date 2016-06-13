package com.everhomes.rest.techpark.park;

import java.sql.Timestamp;


/**
 * <ul>
 *  <li>applyStatus: 申请状态</li>
 *  <li>applierName: 申请人姓名</li>
 *  <li>applierPhone: 申请人手机号</li>
 *  <li>plateNumber: 车牌号</li>
 *  <li>beginDay: 查询申请时间开始日期</li>
 *  <li>endDay: 查询申请时间结束日期</li>
 *  <li>communityId: 园区id</li>
 * </ul>
 *
 */
public class SearchApplyCardCommand {
	
	private Byte applyStatus;
	
	private String applierName;
	
	private String applierPhone;
	
	private String plateNumber;
	
	private String beginDay;
	
	private String endDay;
	
	private Long communityId;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Byte getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Byte applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getApplierPhone() {
		return applierPhone;
	}

	public void setApplierPhone(String applierPhone) {
		this.applierPhone = applierPhone;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getBeginDay() {
		return beginDay;
	}

	public void setBeginDay(String beginDay) {
		this.beginDay = beginDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
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

}
