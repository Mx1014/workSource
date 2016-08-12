package com.everhomes.rest.equipment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>eqStandards: 标准列表 参考{@link com.everhomes.rest.equipment.EquipmentStandardsDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class SearchEquipmentStandardsResponse {
	
	@ItemType(EquipmentStandardsDTO.class)
	private List<EquipmentStandardsDTO> eqStandards;
	
	private Long nextPageAnchor;
	
	public SearchEquipmentStandardsResponse(Long nextPageAnchor, List<EquipmentStandardsDTO> eqStandards) {
        this.nextPageAnchor = nextPageAnchor;
        this.eqStandards = eqStandards;
    }
	
	public List<EquipmentStandardsDTO> getEqStandards() {
		return eqStandards;
	}

	public void setEqStandards(List<EquipmentStandardsDTO> eqStandards) {
		this.eqStandards = eqStandards;
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
