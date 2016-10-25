package com.everhomes.rest.equipment;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 巡检项id</li>
 *  <li>ownerId: 巡检项所属组织等的id</li>
 *  <li>ownerType: 巡检项所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>name: 巡检项名称</li>
 *  <li>valueType: 巡检项类型0-none、1-two-tuple、2-range</li>
 *  <li>unit: 单位</li>
 *  <li>valueJason: 值，包含参考值referenceValue和偏差范围offsetRange</li>
 * </ul>
 */
public class InspectionItemDTO {
	
	private Long id;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private String name;
	
	private Byte valueType;
	
	private String unit;
	
	private String valueJason;

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public Byte getValueType() {
		return valueType;
	}

	public void setValueType(Byte valueType) {
		this.valueType = valueType;
	}

	public String getValueJason() {
		return valueJason;
	}

	public void setValueJason(String valueJason) {
		this.valueJason = valueJason;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
