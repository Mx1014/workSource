package com.everhomes.rest.quality;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，com.everhomes.rest.quality.OwnerType</li>
 *  <li>name: 标准名称</li>
 *  <li>standardNumber: 标准编号</li>
 *  <li>description: 具体内容</li>
 *  <li>categoryId: category表的id</li>
 *  <li>repeat: 执行周期 com.everhomes.rest.quality.RepeatSettingDTO</li>
 *  <li>group: 业务组信息 com.everhomes.rest.quality.StandardGroupDTO</li>
 * </ul>
 */
public class CreatQualityStandardCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private String name;
	
	private String standardNumber;
	
	private String description;
	
	private Long categoryId;
	
	private RepeatSettingsDTO repeat;
	
	@ItemType(StandardGroupDTO.class)
	private List<StandardGroupDTO> group;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public RepeatSettingsDTO getRepeat() {
		return repeat;
	}

	public void setRepeat(RepeatSettingsDTO repeat) {
		this.repeat = repeat;
	}

	public List<StandardGroupDTO> getGroup() {
		return group;
	}

	public void setGroup(List<StandardGroupDTO> group) {
		this.group = group;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
