package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>eqAccessories: 备品配件信息 参考{@link com.everhomes.rest.equipment.EquipmentAccessoriesDTO}</li>
 *  <li>quantity: 数量</li>
 *  <li>equipmentId: 设备id</li>
 *  <li>id: 主键id</li>
 * </ul>
 */
public class EquipmentAccessoryMapDTO {
	
	private EquipmentAccessoriesDTO eqAccessories;
	
	private Integer quantity;
	
	private Long equipmentId;
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EquipmentAccessoriesDTO getEqAccessories() {
		return eqAccessories;
	}

	public void setEqAccessories(EquipmentAccessoriesDTO eqAccessories) {
		this.eqAccessories = eqAccessories;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
