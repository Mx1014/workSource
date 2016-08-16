package com.everhomes.rest.pmtask;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class CommunityTaskLogDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long repairId;
	private String content;
	private Byte status;
	private Long targetId;
	private Long operatorUid;
	private Timestamp operatorTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Long getRepairId() {
		return repairId;
	}
	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public Long getOperatorUid() {
		return operatorUid;
	}
	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}
	public Timestamp getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(Timestamp operatorTime) {
		this.operatorTime = operatorTime;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
