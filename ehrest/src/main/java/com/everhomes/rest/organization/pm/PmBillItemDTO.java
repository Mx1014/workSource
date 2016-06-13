package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id : 账单明细id</li>
 *	<li>billId : 账单id</li>
 *	<li>itemName : 明细名称</li>
 *	<li>startCount : 开始计数</li>
 *	<li>endCount : 结束计数</li>
 *	<li>useCount : 使用量</li>
 *	<li>price : 单价</li>
 *	<li>totalAmount : 总额</li>
 *	<li>description : 描述</li>
 *	<li>creatorUid : 创建者id</li>
 *	<li>createTime : 创建时间</li>
 *</ul>
 *
 */
public class PmBillItemDTO {
	
	private java.lang.Long       id;
	private java.lang.Long       billId;
	private java.lang.String     itemName;
	private java.math.BigDecimal startCount;
	private java.math.BigDecimal endCount;
	private java.math.BigDecimal useCount;
	private java.math.BigDecimal price;
	private java.math.BigDecimal totalAmount;
	private java.lang.String     description;
	private java.lang.Long       creatorUid;
	private java.lang.Long   createTime;
	
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getBillId() {
		return billId;
	}
	public void setBillId(java.lang.Long billId) {
		this.billId = billId;
	}
	public java.lang.String getItemName() {
		return itemName;
	}
	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}
	public java.math.BigDecimal getStartCount() {
		return startCount;
	}
	public void setStartCount(java.math.BigDecimal startCount) {
		this.startCount = startCount;
	}
	public java.math.BigDecimal getEndCount() {
		return endCount;
	}
	public void setEndCount(java.math.BigDecimal endCount) {
		this.endCount = endCount;
	}
	public java.math.BigDecimal getUseCount() {
		return useCount;
	}
	public void setUseCount(java.math.BigDecimal useCount) {
		this.useCount = useCount;
	}
	public java.math.BigDecimal getPrice() {
		return price;
	}
	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}
	public java.math.BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(java.math.BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public java.lang.String getDescription() {
		return description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	public java.lang.Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(java.lang.Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public java.lang.Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.lang.Long createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
