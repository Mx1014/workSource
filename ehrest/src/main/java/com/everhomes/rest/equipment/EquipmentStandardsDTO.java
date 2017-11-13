package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *  <li>id: 标准id</li>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>name: 标准名称</li>
 *  <li>repeatType: 0:no repeat 1: by day 2:by week 3: by month 4:year</li>
 *  <li>standardNumber: 标准编号</li>
 *  <li>standardSource: 标准来源</li>
 *  <li>status: 标准状态 参考{@link com.everhomes.rest.equipment.EquipmentStandardStatus}</li>
 *  <li>equipmentsCount: 使用此标准的设备数</li>
 *  <li>items: 标准关联的巡检项目 参考{@link com.everhomes.rest.equipment.InspectionItemDTO}</li>
 *  <li>equipmentsDTO: 标准关联的设备列表 参考{@link com.everhomes.rest.equipment.EquipmentsDTO}</li>
 *  <li>creatorUid: 创建该标准的用户id</li>
 *  <li>createTime: 创建该标准的时间</li>
 *  <li>operatorUid: 最后对该标准进行修改的用户id</li>
 *  <li>operatorName: 最后对该标准进行修改的用户姓名</li>
 *  <li>updateTime: 更新该标准的时间</li>
 *  <li>deleterUid: 删除该标准的用户id</li>
 *  <li>deleteTime: 删除该标准的时间</li>
 * </ul>
 */
public class EquipmentStandardsDTO {
	
	private Long id;

	private String name;

	private  Byte repeatType;
	
	private String ownerType;
	
	private Long ownerId;
	
	private String standardNumber;
	
	private String standardSource;

	@Deprecated
	private String remarks;

	@Deprecated
	private String description;
	
	private Byte standardType;

	@Deprecated
	@ItemType(RepeatSettingsDTO.class)
	private RepeatSettingsDTO repeat;

	@ItemType(InspectionItemDTO.class)
	private  List<InspectionItemDTO>  items;

	@ItemType(EquipmentsDTO.class)
	private List<EquipmentsDTO> equipments;
	
	private Integer equipmentsCount;
	
	private Byte status;
	
	private Long creatorUid;
	
	private Timestamp createTime;
	
	private Long operatorUid;
	
	private String operatorName;
	
	private Timestamp updateTime;
	
	private Long deleterUid;
	
	private Timestamp deleteTime;

	@Deprecated
	private Long templateId;

	@Deprecated
	private String templateName;

	@Deprecated
	private Integer reviewExpiredDays;

	@Deprecated
	@ItemType(StandardGroupDTO.class)
	private List<StandardGroupDTO> executiveGroup;

	@Deprecated
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

	public Byte getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(Byte repeatType) {
		this.repeatType = repeatType;
	}

	public List<InspectionItemDTO> getItems() {
		return items;
	}

	public void setItems(List<InspectionItemDTO> items) {
		this.items = items;
	}

	public List<EquipmentsDTO> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentsDTO> equipments) {
		this.equipments = equipments;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
