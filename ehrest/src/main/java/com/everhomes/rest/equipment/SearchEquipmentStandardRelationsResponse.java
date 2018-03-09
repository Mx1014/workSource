package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>relations: 设备-标准关系列表  用于新建计划选择关联巡检对象  参考{@link com.everhomes.rest.equipment.EquipmentStandardRelationDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class SearchEquipmentStandardRelationsResponse {

	@ItemType(EquipmentStandardRelationDTO.class)
	private List<EquipmentStandardRelationDTO> relations;
	
	private Long nextPageAnchor;
	
	public List<EquipmentStandardRelationDTO> getRelations() {
		return relations;
	}

	public void setRelations(List<EquipmentStandardRelationDTO> relations) {
		this.relations = relations;
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
