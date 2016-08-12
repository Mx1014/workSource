package com.everhomes.rest.equipment;


import javax.validation.constraints.NotNull;


import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>name: 标准名称</li>
 *  <li>standardNumber: 标准编号</li>
 *  <li>standardSource: 标准来源</li>
 *  <li>description: 具体内容</li>
 *  <li>remarks: 备注</li>
 *  <li>standardType: 标准类别 参考{@link com.everhomes.rest.equipment.StandardType}</li>
 *  <li>repeat: 执行周期 参考{@link com.everhomes.rest.repeat.RepeatSettingsDTO}</li>
 * </ul>
 */
public class CreatEquipmentStandardCommand {
	
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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
