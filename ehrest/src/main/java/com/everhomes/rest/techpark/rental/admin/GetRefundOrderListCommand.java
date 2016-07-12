package com.everhomes.rest.techpark.rental.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>查询订单 
 * <li>launchPadItemId：图标id</li> 
 * <li>startTime：开始时间</li> 
 * <li>endTime：结束时间</li> 
 * <li>vendorType：支付方式,10001-支付宝，10002-微信</li> 
* <li>status：订单状态 •REFUNDING(9): 退款中 •REFUNDED(10): 已退款 参考{@link com.everhomes.rest.techpark.rental.SiteBillStatus}</li>   
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class GetRefundOrderListCommand {  
	private java.lang.Long       launchPadItemId; 
	private Long startTime; 
	private Long endTime;
	private java.lang.String     vendorType;
	private Byte status;  
	private Long pageAnchor;
	private Integer pageSize;
	
	@Override
    public String toString() {

		
        return StringHelper.toJsonString(this);
    }
 
	public java.lang.Long getLaunchPadItemId() {
		return launchPadItemId;
	}

	public void setLaunchPadItemId(java.lang.Long launchPadItemId) {
		this.launchPadItemId = launchPadItemId;
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

	public java.lang.String getVendorType() {
		return vendorType;
	}

	public void setVendorType(java.lang.String vendorType) {
		this.vendorType = vendorType;
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

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
 
	  
 


}
