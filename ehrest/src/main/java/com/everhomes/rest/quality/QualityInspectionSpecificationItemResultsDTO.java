package com.everhomes.rest.quality;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: id</li>
 *  <li>ownerId: 所属的主体id</li>
 *  <li>ownerType: 所属的主体，com.everhomes.rest.quality.OwnerType</li>
 *  <li>targetId: 所属的项目id</li>
 *  <li>targetType: 所属项目类型</li>
 *  <li>taskRecordId: 任务记录id</li>
 *  <li>taskId:任务id</li>
 *  <li>specificationParentId: 规范事项所属规范id</li>
 *  <li>specificationParentName: 规范事项所属规范名</li>
 *  <li>specificationId: 规范事项id</li>
 *  <li>specificationName: 规范事项名</li>
 *  <li>itemDescription: 规范事项描述</li>
 *  <li>itemScore: 事项分数</li>
 *  <li>quantity: 数量</li>
 *  <li>totalScore: 总分(事项分数*数量)</li>
 *  <li>creatorUid: 创建者id</li>
 *  <li>createTime: 创建时间</li>
 * </ul>
 */
public class QualityInspectionSpecificationItemResultsDTO {
	 
	private Long id;
	  
	private String ownerType;
	  
	private Long ownerId;
	 
	private Long targetId;
	  
	private String targetType;
	  
	private Long taskRecordId;
	  
	private Long taskId;
	  
	private Long specificationParentId;
	
	private String specificationParentName;
	  
	private Long specificationId;
	
	private String specificationName;
	  
	private String itemDescription;
	  
	private Double itemScore;
	  
	private Integer quantity;
	  
	private Double totalScore;
	  
	private Long creatorUid;
	  
	private Timestamp createTime;
	  
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

	public Long getTaskRecordId() {
		return taskRecordId;
	}

	public void setTaskRecordId(Long taskRecordId) {
		this.taskRecordId = taskRecordId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getSpecificationParentId() {
		return specificationParentId;
	}

	public void setSpecificationParentId(Long specificationParentId) {
		this.specificationParentId = specificationParentId;
	}

	public Long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
	}

	public String getSpecificationParentName() {
		return specificationParentName;
	}

	public void setSpecificationParentName(String specificationParentName) {
		this.specificationParentName = specificationParentName;
	}

	public String getSpecificationName() {
		return specificationName;
	}

	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Double getItemScore() {
		return itemScore;
	}

	public void setItemScore(Double itemScore) {
		this.itemScore = itemScore;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
