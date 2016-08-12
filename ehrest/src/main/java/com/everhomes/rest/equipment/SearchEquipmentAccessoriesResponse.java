package com.everhomes.rest.equipment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accessories: 设备备件列表 参考{@link com.everhomes.rest.equipment.EquipmentAccessoriesDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class SearchEquipmentAccessoriesResponse {
	@ItemType(EquipmentAccessoriesDTO.class)
	private List<EquipmentAccessoriesDTO> accessories;
	
	private Long nextPageAnchor;
	
	public List<EquipmentAccessoriesDTO> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<EquipmentAccessoriesDTO> accessories) {
		this.accessories = accessories;
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
