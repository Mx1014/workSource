// @formatter:off
package com.everhomes.rest.organization.pm;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>billId: 账单id</li>
 * <li>itemName: 账单项名称</li>
 * <li>startCount: 起始数量</li>
 * <li>endCount: 已使用总数量</li>
 * <li>useCount: 当前使用量</li>
 * <li>price: 价格</li>
 * <li>totalAmount: 小计</li>
 * <li>description: 描述</li>
 * <li>creatorUid: 创建者id</li>
 * <li>createTime: 创建时间</li>
 * 
 * </ul>
 */
public class PropBillItemDTO {
	private Long       id;
	private Long       billId;
	private String     itemName;
	private BigDecimal startCount;
	private BigDecimal endCount;
	private BigDecimal useCount;
	private BigDecimal price;
	private BigDecimal totalAmount;
	private String     description;
	private Long       creatorUid;
	private java.sql.Timestamp   createTime;
	
    public PropBillItemDTO() {
    }
    
  

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getBillId() {
		return billId;
	}



	public void setBillId(Long billId) {
		this.billId = billId;
	}



	public String getItemName() {
		return itemName;
	}



	public void setItemName(String itemName) {
		this.itemName = itemName;
	}



	public BigDecimal getStartCount() {
		return startCount;
	}



	public void setStartCount(BigDecimal startCount) {
		this.startCount = startCount;
	}



	public BigDecimal getEndCount() {
		return endCount;
	}



	public void setEndCount(BigDecimal endCount) {
		this.endCount = endCount;
	}



	public BigDecimal getUseCount() {
		return useCount;
	}



	public void setUseCount(BigDecimal useCount) {
		this.useCount = useCount;
	}



	public BigDecimal getPrice() {
		return price;
	}



	public void setPrice(BigDecimal price) {
		this.price = price;
	}



	public BigDecimal getTotalAmount() {
		return totalAmount;
	}



	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public Long getCreatorUid() {
		return creatorUid;
	}



	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}



	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}



	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
