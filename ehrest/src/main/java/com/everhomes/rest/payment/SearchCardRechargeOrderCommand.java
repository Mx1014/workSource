package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>startDate: 开始日期</li>
 * <li>endDate: 结束日期</li>
 * <li>rechargeType: 充值方式  10001-支付宝，10002-微信</li>
 * <li>rechargeStatus: 0:充值失败   1:处理中  2:充值成功 3:处理完成  4:已退款{@link com.everhomes.rest.payment.CardRechargeStatus}</li>
 * <li>condition: 手机号或者姓名</li>
 * <li>pageAnchor: 分页的瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class SearchCardRechargeOrderCommand {
	private String ownerType;
    private Long ownerId;
    private Long startDate;
    private Long endDate;
    private String rechargeType;
    private byte rechargeStatus;
    private String condition;
    private Long pageAnchor;
    private Integer pageSize;
    
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
	public Long getStartDate() {
		return startDate;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	public String getRechargeType() {
		return rechargeType;
	}
	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}
	
	public byte getRechargeStatus() {
		return rechargeStatus;
	}
	public void setRechargeStatus(byte rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
