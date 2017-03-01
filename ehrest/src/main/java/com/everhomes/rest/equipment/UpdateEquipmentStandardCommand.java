package com.everhomes.rest.equipment;

import java.util.List;

import javax.validation.constraints.NotNull;

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
 *  <li>templateId: 巡检项模板id</li>
 *  <li>reviewExpiredDays: 审批过期时间限制（天）</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 * </ul>
 */
public class UpdateEquipmentStandardCommand {
	
	private Long id;

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private String name;
	
	private String standardNumber;
	
	private String standardSource;
	
	private String description;
	
	private String remarks;
	
	private Byte standardType;
	
	private RepeatSettingsDTO repeat;
	
	private Long templateId;
	
	private Integer reviewExpiredDays;
	
	private Long inspectionCategoryId;
	
	@ItemType(StandardGroupDTO.class)
	private List<StandardGroupDTO> group;
	
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
