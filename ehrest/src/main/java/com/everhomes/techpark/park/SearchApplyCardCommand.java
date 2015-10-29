package com.everhomes.techpark.park;

import java.sql.Timestamp;


/**
 * <ul>
 *  <li>applyStatus: 申请状态</li>
 *  <li>applierName: 申请人姓名</li>
 *  <li>applierPhone: 申请人手机号</li>
 *  <li>plateNumber: 车牌号</li>
 *  <li>beginDay: 查询申请时间开始日期</li>
 *  <li>endDay: 查询申请时间结束日期</li>
 * </ul>
 *
 */
public class SearchApplyCardCommand {
	
	private Byte applyStatus;
	
	private String applierName;
	
	private String applierPhone;
	
	private String plateNumber;
	
	private Timestamp beginDay;
	
	private Timestamp endDay;
	
	private Long pageAnchor;
	
	private Integer pageSize;

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

	public Timestamp getBeginDay() {
		return beginDay;
	}

	public void setBeginDay(Timestamp beginDay) {
		this.beginDay = beginDay;
	}

	public Timestamp getEndDay() {
		return endDay;
	}

	public void setEndDay(Timestamp endDay) {
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
