package com.everhomes.rest.equipment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>equipment: 设备列表 参考{@link com.everhomes.rest.equipment.EquipmentsDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class SearchEquipmentsResponse {
	@ItemType(EquipmentsDTO.class)
	private List<EquipmentsDTO> equipment;
	
	private Long nextPageAnchor;
	
	public List<EquipmentsDTO> getEquipment() {
		return equipment;
	}

	public void setEquipment(List<EquipmentsDTO> equipment) {
		this.equipment = equipment;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
