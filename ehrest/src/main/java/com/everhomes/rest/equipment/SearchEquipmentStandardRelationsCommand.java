package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 设备-标准关联所属组织等的id</li>
 *  <li>ownerType: 设备-标准关联所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetType: 设备-标准关联所属管理处类型</li>
 *  <li>targetId: 设备-标准关联所属管理处</li>
 *  <li>reviewStatus: 设备-标准关联状态，参考{@link com.everhomes.rest.equipment.EquipmentReviewStatus}</li>
 *  <li>reviewStatus: 设备-标准关联审批结果，参考{@link com.everhomes.rest.equipment.ReviewResult}</li>
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
	
	private Byte reviewStatus;

	private Byte reviewResult;

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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
