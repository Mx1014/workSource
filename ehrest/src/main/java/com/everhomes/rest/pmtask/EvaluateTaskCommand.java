package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>id: 任务ID</li>
 * <li>star: 评价分数</li>
 * </ul>
 */
public class EvaluateTaskCommand {
	private String ownerType;
    private Long ownerId;
    private Long id;
    private Byte star;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
	public Byte getStar() {
		return star;
	}
	public void setStar(Byte star) {
		this.star = star;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
