package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 设备所属组织等的id</li>
 *  <li>ownerType: 设备所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 设备所属管理处id</li>
 *  <li>targetType: 设备所属管理处类型</li>
 *  <li>categoryId: 设备类型id</li>
 *  <li>status: 设备状态 参考{@link com.everhomes.rest.equipment.EquipmentStatus}</li>
 *  <li>relatedStatus: 设备关联状态  0-未关联 1-待审批 2-审批通过 3-审批不通过</li>
 *  <li>keyword: 查询关键字</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 * </ul>
 */
public class SearchEquipmentsCommand {

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;
	
	private String targetType;
	
	private Byte relatedStatus;
	
	private Byte status;
	
	private Long categoryId;
	
	private String keyword;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Byte getRelatedStatus() {
		return relatedStatus;
	}

	public void setRelatedStatus(Byte relatedStatus) {
		this.relatedStatus = relatedStatus;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
