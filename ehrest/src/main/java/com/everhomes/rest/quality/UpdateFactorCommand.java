package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
          
/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>ownerId: 任务所属组织等的id</li>
 *  <li>ownerType: 任务所属组织类型，如enterprise</li>
 *  <li>groupId: 业务组id</li>
 *  <li>categoryId: category的主键id</li>
 *  <li>weight: 权重</li>
 * </ul>
 */
public class UpdateFactorCommand {
	
	private Long id;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	@NotNull
	private Long groupId;
	
	@NotNull
	private Long categoryId;
	
	@NotNull
	private Double weight;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
