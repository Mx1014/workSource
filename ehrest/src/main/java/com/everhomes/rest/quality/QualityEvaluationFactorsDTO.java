package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>groupId: 业务组id</li>
 *  <li>categoryId: category的主键id</li>
 *  <li>weight: 权重</li>
 * </ul>
 */
public class QualityEvaluationFactorsDTO {
	
	private Long id;
	
	private Long categoryId;
	
	private Long groupId;
	
	private Double weight;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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
