package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>ownerId: 标准所属组织等的id</li>
 *  <li>ownerType: 标准所属组织类型，如enterprise</li>
 *  <li>targetId: 标准所属的项目id</li>
 *  <li>targetType: 标准所属项目类型</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>reviewResult: 标准审阅结果 {@link com.everhomes.rest.equipment.ReviewResult}</li>
 *  <li>planCondition: 搜索条件</li>
 * </ul>
 */
public class ListQualityStandardsCommand {
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Long targetId;
	@ItemType(Long.class)
	private List<Long> targetIds;
	
	private String targetType;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private Byte reviewResult;

	private String planCondition;
	
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

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}

	public String getPlanCondition() {
		return planCondition;
	}

	public void setPlanCondition(String planCondition) {
		this.planCondition = planCondition;
	}

	public List<Long> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<Long> targetIds) {
		this.targetIds = targetIds;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
