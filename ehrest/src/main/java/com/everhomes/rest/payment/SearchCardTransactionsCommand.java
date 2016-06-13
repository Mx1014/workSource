package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>startDate: 开始日期</li>
 * <li>endDate: 结束日期</li>
 * <li>consumeType: 消费方式</li>
 * <li>status: 0:支付失败   1:过期  2:已撤销 3:支付成功  4:未支付{@link com.everhomes.rest.payment.CardTransactionStatus}</li>
 * <li>keyword: 手机号或者姓名</li>
 * <li>pageAnchor: 分页的瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class SearchCardTransactionsCommand {
	private String ownerType;
    private Long ownerId;
    private Long startDate;
    private Long endDate;
    private String consumeType;
    private Byte status;
    private String keyword;
    private Integer pageSize;
    private Long pageAnchor;
    
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
	
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
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
	public String getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}
	
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
