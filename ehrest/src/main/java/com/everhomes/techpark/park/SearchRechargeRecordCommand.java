package com.everhomes.techpark.park;

/**
 * <ul>
 *  <li>ownerName: 车主姓名</li>
 *  <li>rechargePhone: 充值人人手机号</li>
 *  <li>plateNumber: 车牌号</li>
 *  <li>enterpriseCommunityId: 园区id</li>
 * </ul>
 *
 */
public class SearchRechargeRecordCommand {
	
	private String ownerName;
	
	private String rechargePhone;
	
	private String plateNumber;
	
	private Long enterpriseCommunityId;
	
	private Long pageAnchor;
	
	private Integer pageSize; 

	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
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
	
}
