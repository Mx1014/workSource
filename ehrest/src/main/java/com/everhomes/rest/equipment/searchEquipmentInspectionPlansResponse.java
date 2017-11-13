package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>equipmentInspectionPlanDTO: 设备备件列表 参考{@link com.everhomes.rest.equipment.EquipmentInspectionPlanDTO}</li>
 * <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */

public class searchEquipmentInspectionPlansResponse {

    @ItemType(EquipmentInspectionPlanDTO.class)
    private List<EquipmentInspectionPlanDTO> equipmentInspectionPlanDTO;

    private Long nextPageAnchor;

    public List<EquipmentInspectionPlanDTO> getEquipmentInspectionPlanDTO() {
        return equipmentInspectionPlanDTO;
    }

    public void setEquipmentInspectionPlanDTO(List<EquipmentInspectionPlanDTO> equipmentInspectionPlanDTO) {
        this.equipmentInspectionPlanDTO = equipmentInspectionPlanDTO;
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
