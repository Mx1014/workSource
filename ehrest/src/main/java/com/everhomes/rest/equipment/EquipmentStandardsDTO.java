package com.everhomes.rest.equipment;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 标准id</li>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>name: 标准名称</li>
 *  <li>standardNumber: 标准编号</li>
 *  <li>standardSource: 标准来源</li>
 *  <li>description: 具体内容</li>
 *  <li>remarks: 备注</li>
 *  <li>standardType: 标准类别 参考{@link com.everhomes.rest.equipment.StandardType}</li>
 *  <li>repeat: 执行周期 参考{@link com.everhomes.rest.repeat.RepeatSettingsDTO}</li>
 *  <li>status: 标准状态 参考{@link com.everhomes.rest.equipment.EquipmentStandardStatus}</li>
 *  <li>equipmentsCount: 使用此标准的设备数</li>
 *  <li>creatorUid: 创建该标准的用户id</li>
 *  <li>createTime: 创建该标准的时间</li>
 *  <li>operatorUid: 最后对该标准进行修改的用户id</li>
 *  <li>operatorName: 最后对该标准进行修改的用户姓名</li>
 *  <li>updateTime: 更新该标准的时间</li>
 *  <li>deleterUid: 删除该标准的用户id</li>
 *  <li>deleteTime: 删除该标准的时间</li>
 *  <li>templateId: 巡检项模板id</li>
 *  <li>templateName: 巡检项模板名称</li>
 *  <li>reviewExpiredDays: 审批过期时间限制（天）</li>
 * </ul>
 */
public class EquipmentStandardsDTO {
	
	private Long id;

	private String name;
	
	private String ownerType;
	
	private Long ownerId;
	
	private String standardNumber;
	
	private String standardSource;
	
	private String remarks;
	
	private String description;
	
	private Byte standardType;
	
	@ItemType(RepeatSettingsDTO.class)
	private RepeatSettingsDTO repeat;
	
	private Integer equipmentsCount;
	
	private Byte status;
	
	private Long creatorUid;
	
	private Timestamp createTime;
	
	private Long operatorUid;
	
	private String operatorName;
	
	private Timestamp updateTime;
	
	private Long deleterUid;
	
	private Timestamp deleteTime;

	private Long templateId;
	
	private String templateName;
	
	private Integer reviewExpiredDays;
	
	@ItemType(StandardGroupDTO.class)
	private List<StandardGroupDTO> executiveGroup;

	@ItemType(StandardGroupDTO.class)
	private List<StandardGroupDTO> reviewGroup;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getStandardNumber() {
		return standardNumber;
	}

	public void setStandardNumber(String standardNumber) {
		this.standardNumber = standardNumber;
	}

	public String getStandardSource() {
		return standardSource;
	}

	public void setStandardSource(String standardSource) {
		this.standardSource = standardSource;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getStandardType() {
		return standardType;
	}

	public void setStandardType(Byte standardType) {
		this.standardType = standardType;
	}

	public RepeatSettingsDTO getRepeat() {
		return repeat;
	}

	public void setRepeat(RepeatSettingsDTO repeat) {
		this.repeat = repeat;
	}

	public Integer getEquipmentsCount() {
		return equipmentsCount;
	}

	public void setEquipmentsCount(Integer equipmentsCount) {
		this.equipmentsCount = equipmentsCount;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getDeleterUid() {
		return deleterUid;
	}

	public void setDeleterUid(Long deleterUid) {
		this.deleterUid = deleterUid;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getReviewExpiredDays() {
		return reviewExpiredDays;
	}

	public void setReviewExpiredDays(Integer reviewExpiredDays) {
		this.reviewExpiredDays = reviewExpiredDays;
	}

	public List<StandardGroupDTO> getExecutiveGroup() {
		return executiveGroup;
	}

	public void setExecutiveGroup(List<StandardGroupDTO> executiveGroup) {
		this.executiveGroup = executiveGroup;
	}

	public List<StandardGroupDTO> getReviewGroup() {
		return reviewGroup;
	}

	public void setReviewGroup(List<StandardGroupDTO> reviewGroup) {
		this.reviewGroup = reviewGroup;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
