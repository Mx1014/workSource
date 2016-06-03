package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>startDate: 开始日期</li>
 * <li>endDate: 结束日期</li>
 * <li>consumeType: 消费方式</li>
 * <li>status: 00: 处理中
 	01：成功
 	02：失败
	03：已撤销
	04：已冲正
	05: 已退货
	06: 已调账
	07: 已部分退货
	{@link com.everhomes.rest.payment.CardTranscationStatus}</li>
 * <li>condition: 手机号或者姓名</li>
 * <li>nextPageAnchor: 分页的瞄</li>
 * <li>pageSize: 每页条数</li>
 * </ul>
 */
public class SearchCardTranscationsCommand {
	private String ownerType;
    private Long ownerId;
    private Long startDate;
    private Long endDate;
    private String consumeType;
    private byte status;
    private String condition;
    private Integer pageSize;
    private Long nextPageAnchor;
    
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
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
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
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
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
