package com.everhomes.rest.quality;



import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>templateId: 从模板进入时填模板id</li>
 *  <li>sampleId: 从例行检查进入时填例行检查id</li>
 *  <li>ownerId: 任务所属的主体id</li>
 *  <li>ownerType: 任务所属的主体，com.everhomes.rest.quality.OwnerType</li>
 *  <li>targetId: 任务所属的项目id</li>
 *  <li>targetType: 任务所属的项目，com.everhomes.rest.quality.OwnerType</li>
 *  <li>name: 任务名称</li>
 *  <li>specificationId: specification表中的id</li>
 *  <li>executorId: 执行人uid</li>
 *  <li>group: 业务组信息 com.everhomes.rest.quality.StandardGroupDTO</li>
 *  <li>executiveExpireTime: 任务截止时间</li>
 *  <li>templateFlag: 是否存为模板 true: 是; false: 否</li>
 * </ul>
 */
public class CreateQualityInspectionTaskCommand {
	
	private Long templateId;

	private Long sampleId;

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	@NotNull
	private Long targetId;
	
	@NotNull
	private String targetType;
	
	private String name;
	
	@NotNull
	private Long specificationId;
	
	@NotNull
	@ItemType(StandardGroupDTO.class)
	private StandardGroupDTO group;

	private Long executiveExpireTime;
	
	private Long executorId;
	
	private Boolean templateFlag;

	public Long getSampleId() {
		return sampleId;
	}

	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Boolean getTemplateFlag() {
		return templateFlag;
	}

	public void setTemplateFlag(Boolean templateFlag) {
		this.templateFlag = templateFlag;
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
	public Long getSpecificationId() {
		return specificationId;
	}


	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
	}


	public StandardGroupDTO getGroup() {
		return group;
	}


	public void setGroup(StandardGroupDTO group) {
		this.group = group;
	}


	public Long getExecutiveExpireTime() {
		return executiveExpireTime;
	}


	public void setExecutiveExpireTime(Long executiveExpireTime) {
		this.executiveExpireTime = executiveExpireTime;
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


	public Long getExecutorId() {
		return executorId;
	}


	public void setExecutorId(Long executorId) {
		this.executorId = executorId;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
