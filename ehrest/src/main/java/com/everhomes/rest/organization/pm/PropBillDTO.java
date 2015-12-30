// @formatter:off
package com.everhomes.rest.organization.pm;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>communityId: 小区id</li>
 * <li>entityType: 实体类型</li>
 * <li>entityId: 实体id</li>
 * <li>address: 账单地址</li>
 * <li>name: 账单名称</li>
 * <li>dateStr: 账单时间</li>
 * <li>totalAmount: 金额</li>
 * <li>description: 描述</li>
 * <li>creatorUid: 创建者id</li>
 * <li>createTime: 创建时间</li>
 * <li>notifyCount: 通知总数</li>
 * <li>notifyTime: 通知时间</li>
 * <li>itemList: 具体账单项列表</li>
 * </ul>
 */
public class PropBillDTO {
	private Long       id;
	private Long       communityId;
	private String     entityType;
	private Long       entityId;
	private String     address;
	private String     name;
	private String     dateStr;
	private BigDecimal totalAmount;
	private String     description;
	private Long       creatorUid;
	private Timestamp   createTime;
	private Integer    notifyCount;
	private Timestamp   notifyTime;
	
	@ItemType(PropBillItemDTO.class)
	private List<PropBillItemDTO> itemList;
	
    public PropBillDTO() {
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getNotifyCount() {
		return notifyCount;
	}

	public void setNotifyCount(Integer notifyCount) {
		this.notifyCount = notifyCount;
	}

	public Timestamp getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(Timestamp notifyTime) {
		this.notifyTime = notifyTime;
	}
	
	public List<PropBillItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<PropBillItemDTO> itemList) {
		this.itemList = itemList;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
