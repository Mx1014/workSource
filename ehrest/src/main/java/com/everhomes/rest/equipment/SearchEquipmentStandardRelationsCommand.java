package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>ownerId: 设备-标准关联所属组织等的id</li>
 *  <li>ownerType: 设备-标准关联所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetType: 设备-标准关联所属管理处类型</li>
 *  <li>targetId: 设备-标准关联所属管理处</li>
 *  <li>repeatType: 巡检对象对应标准周期类型</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>keyword: 查询关键字</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 * </ul>
 */
public class SearchEquipmentStandardRelationsCommand {
	@NotNull
	private Long ownerId;

	@NotNull
	private String ownerType;

	private Long targetId;

	private String targetType;

	private  Byte repeatType;

	@Deprecated
	private Byte reviewStatus;

	@Deprecated
	private Byte reviewResult;

	private String keyword;

	private Long pageAnchor;

	private Integer pageSize;

	//V3.0.2 增加 用于创建计划时候选择设备标准关联
    private Long inspectionCategoryId;

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

	public Byte getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Byte reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
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

	public Byte getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(Byte repeatType) {
		this.repeatType = repeatType;
	}

    public Long getInspectionCategoryId() {
        return inspectionCategoryId;
    }

    public void setInspectionCategoryId(Long inspectionCategoryId) {
        this.inspectionCategoryId = inspectionCategoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
