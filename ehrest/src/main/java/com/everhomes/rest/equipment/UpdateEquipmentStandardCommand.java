package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>id: 标准id</li>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 标准所属项目id</li>
 *  <li>targetType: 标准所属项目类型</li>
 *  <li>name: 标准名称</li>
 *  <li>repeatType: 0:no repeat 1: by day 2:by week 3: by month 4:year</li>
 *  <li>standardNumber: 标准编号 { 物业巡检V3.2删除}</li>
 *  <li>standardSource: 标准来源</li>
 *  <li>description: 具体内容 { 物业巡检V3.2删除}</li>
 *  <li>remarks: 备注 { 物业巡检V3.2删除}</li>
 *  <li>standardType: 标准类别 参考{@link com.everhomes.rest.equipment.StandardType}</li>
 *  <li>repeat: 执行周期 { 物业巡检V3.2删除} 参考{@link com.everhomes.rest.repeat.RepeatSettingsDTO}</li>
 *  <li>templateId: 巡检项模板id</li>
 *  <li>reviewExpiredDays: 审批过期时间限制（天） { 物业巡检V3.2删除}</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>items: 标准关联的巡检项目 参考{@link com.everhomes.rest.equipment.InspectionItemDTO}</li>
 *  <li>equipmentsIds: 标准关联的设备id列表 </li>
 * </ul>
 */
public class UpdateEquipmentStandardCommand {
	
	private Long id;

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	@NotNull

	private Long targetId;

	@NotNull
	private String targetType;

	private String name;

	private  Byte repeatType ;

	private String standardSource;

	@Deprecated
	private String standardNumber;
	@Deprecated
	private String description;
	@Deprecated
	private String remarks;
	@Deprecated
	private Byte standardType;
	@Deprecated
	@ItemType(RepeatSettingsDTO.class)
	private RepeatSettingsDTO repeat;
	
	private Long templateId;
	@Deprecated
	private Integer reviewExpiredDays;
	
	private Long inspectionCategoryId;
	
	@Deprecated
	@ItemType(StandardGroupDTO.class)
	private List<StandardGroupDTO> group;

	@ItemType(InspectionItemDTO.class)
	private  List<InspectionItemDTO>  items;

	@ItemType(Long.class)
	private List<Long> equipmentsIds;

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

	public List<StandardGroupDTO> getGroup() {
		return group;
	}

	public void setGroup(List<StandardGroupDTO> group) {
		this.group = group;
	}

	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Integer getReviewExpiredDays() {
		return reviewExpiredDays;
	}

	public void setReviewExpiredDays(Integer reviewExpiredDays) {
		this.reviewExpiredDays = reviewExpiredDays;
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

    public List<Long> getEquipmentsIds() {
        return equipmentsIds;
    }

    public void setEquipmentsIds(List<Long> equipmentsIds) {
        this.equipmentsIds = equipmentsIds;
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
