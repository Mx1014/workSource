package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 参考主键id</li>
 *  <li>targetName: 项目名</li>
 *  <li>targetId: 项目id</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>equipmentId: 设备id</li>
 *  <li>equipmentName: 设备名称</li>
 *  <li>standardId: 标准id</li>
 *  <li>standardName: 标准名称</li>
 *  <li>taskCount: 任务总数</li>
 *  <li>toExecuted: 待巡检数</li>
 *  <li>inMaintance: 待维修数</li>
 *  <li>completeInspection: 巡检完成数</li>
 *  <li>completeMaintance: 维修完成数</li>
 *  <li>maintanceRate: 维修率</li>
 * </ul>
 */
public class TaskCountDTO {

	private Long id;
	
	private Long targetId;
	
	private String targetName;
	
	private Long inspectionCategoryId;
	
	private Long equipmentId;
	
	private String equipmentName;
	
	private Long standardId;
	
	private Long standardName;
	
	private Long taskCount;
	
	private Long toExecuted;
	
	private Long inMaintance;
	
	private Long completeInspection;
	
	private Long completeMaintance;
	
	private Double maintanceRate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	public Long getStandardName() {
		return standardName;
	}

	public void setStandardName(Long standardName) {
		this.standardName = standardName;
	}

	public Long getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(Long taskCount) {
		this.taskCount = taskCount;
	}

	public Long getToExecuted() {
		return toExecuted;
	}

	public void setToExecuted(Long toExecuted) {
		this.toExecuted = toExecuted;
	}

	public Long getInMaintance() {
		return inMaintance;
	}

	public void setInMaintance(Long inMaintance) {
		this.inMaintance = inMaintance;
	}

	public Long getCompleteInspection() {
		return completeInspection;
	}

	public void setCompleteInspection(Long completeInspection) {
		this.completeInspection = completeInspection;
	}

	public Long getCompleteMaintance() {
		return completeMaintance;
	}

	public void setCompleteMaintance(Long completeMaintance) {
		this.completeMaintance = completeMaintance;
	}

	public Double getMaintanceRate() {
		return maintanceRate;
	}

	public void setMaintanceRate(Double maintanceRate) {
		this.maintanceRate = maintanceRate;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
