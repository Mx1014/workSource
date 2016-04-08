package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 参考主键id</li>
 *  <li>categoryId: 类型id</li>
 *  <li>categoryName: 类型名</li>
 *  <li>groupName: 业务组名</li>
 *  <li>groupId: 业务组id</li>
 *  <li>weight: 权重</li>
 * </ul>
 */
public class FactorsDTO {

	private Long     id;
	private Long     categoryId;
	private String categoryName;
	private Long     groupId;
	private String groupName;
	private Double   weight;
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
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
