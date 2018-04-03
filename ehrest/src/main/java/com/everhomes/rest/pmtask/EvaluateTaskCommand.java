package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>id: 任务ID</li>
 * <li>star: 服务评价分数</li>
 * <li>operatorStar: 处理人员评价分数</li>
 * </ul>
 */
public class EvaluateTaskCommand {
	private Integer namespaceId;
	private String ownerType;
    private Long ownerId;
    private Long id;
    private Byte star;
    private Byte operatorStar;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
	public Byte getOperatorStar() {
		return operatorStar;
	}
	public void setOperatorStar(Byte operatorStar) {
		this.operatorStar = operatorStar;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
