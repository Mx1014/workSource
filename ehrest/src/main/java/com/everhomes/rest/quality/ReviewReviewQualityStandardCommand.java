package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 标准id</li>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，如QA</li>
 *  <li>targetId: 标准所属的项目id</li>
 *  <li>targetType: 标准所属项目类型</li>
 *  <li>reviewResult: 标准审阅结果 {@link com.everhomes.rest.equipment.ReviewResult}</li>
 * </ul>
 */
public class ReviewReviewQualityStandardCommand {

	private Long id;
	
	private String ownerType;
	
	private Long ownerId;
	
	private Long targetId;
	
	private String targetType;
	
	private Byte reviewResult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
