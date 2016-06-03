package com.everhomes.rest.quality;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>operatorId: 操作人id</li>
 *  <li>operatorName: 操作人名称</li>
 *  <li>operateType: 操作类型：0: none, 1: insert, 2: update, 3: delete</li>
 *  <li>targetType: 目标对象类型，标准的话为standard</li>
 *  <li>targetId: 目标对象id</li>
 *  <li>targetName: 目标对象名称</li>
 *  <li>operateTime: 操作时间</li>
 * </ul>
 */
public class QualityInspectionLogDTO {
	
	private Long operatorId;
	
	private String operatorName;
	
	private Byte operateType;
	
	private String targetType;
	
	private Long targetId;
	
	private String targetName;
	
	private Timestamp operateTime;

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Byte getOperateType() {
		return operateType;
	}

	public void setOperateType(Byte operateType) {
		this.operateType = operateType;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
