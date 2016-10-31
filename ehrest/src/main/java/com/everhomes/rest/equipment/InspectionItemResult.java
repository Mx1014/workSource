package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 巡检项所属组织等的id</li>
 *  <li>ownerType: 巡检项所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>taskId: 任务id</li>
 *  <li>taskLogId: 任务操作记录id</li>
 *  <li>targetType: 对象类型 如equipment</li>
 *  <li>targetId: 对象id</li>
 *  <li>itemId: 巡检项id</li>
 *  <li>itemName: 巡检项名称</li>
 *  <li>itemValueType: 巡检项类型 0-none、1-two-tuple、2-range</li>
 *  <li>itemUnit: 单位</li>
 *  <li>itemValue: 巡检项值</li>
 *  <li>normalFlag: 是否正常</li>
 * </ul>
 */
public class InspectionItemResult {
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Long taskId;
	
	private Long taskLogId;
	
	private String targetType;
	
	private Long targetId;
	
	private Long itemId;
	
	private String itemName;
	
	private Byte itemValueType;
	
	private String itemUnit;
	
	private String itemValue;
	
	private Byte normalFlag;

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

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getTaskLogId() {
		return taskLogId;
	}

	public void setTaskLogId(Long taskLogId) {
		this.taskLogId = taskLogId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
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

	public Byte getItemValueType() {
		return itemValueType;
	}

	public void setItemValueType(Byte itemValueType) {
		this.itemValueType = itemValueType;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public Byte getNormalFlag() {
		return normalFlag;
	}

	public void setNormalFlag(Byte normalFlag) {
		this.normalFlag = normalFlag;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
