package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 标准所属组织等的id</li>
 *  <li>ownerType: 标准所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 标准所属项目id</li>
 *  <li>targetType: 标准所属项目类型</li>
 *  <li>standardType: 标准类别 参考{@link com.everhomes.rest.equipment.StandardType}</li>
 *  <li>status: 标准状态 参考{@link com.everhomes.rest.equipment.EquipmentStandardStatus}</li>
 *  <li>keyword: 查询关键字</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>targetIdFlag: 要不要取没有targetId的数据 参考{@link com.everhomes.rest.equipment.TargetIdFlag}</li>
 * </ul>
 */
public class SearchEquipmentStandardsCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;

	private String targetType;
	
	private Byte standardType;
	
	private Byte status;
	
	private String keyword;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	private Long inspectionCategoryId;

	private Byte targetIdFlag;

	public Byte getTargetIdFlag() {
		return targetIdFlag;
	}

	public void setTargetIdFlag(Byte targetIdFlag) {
		this.targetIdFlag = targetIdFlag;
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

	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
	}
	
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

	public Byte getStandardType() {
		return standardType;
	}

	public void setStandardType(Byte standardType) {
		this.standardType = standardType;
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
