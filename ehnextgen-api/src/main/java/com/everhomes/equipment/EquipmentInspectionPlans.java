package com.everhomes.equipment;

import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.equipment.EquipmentStandardRelationDTO;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionPlans;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Date: 2017/11/13 10 :00
 *
 * @author rui.jia
 */

public class EquipmentInspectionPlans  extends EhEquipmentInspectionPlans{

    private static final long serialVersionUID = -5939045682826070601L;

    private RepeatSettings repeatSettings;

    private List<EquipmentStandardRelationDTO> equipmentStandardRelations;

    public RepeatSettings getRepeatSettings() {
        return repeatSettings;
    }

    public void setRepeatSettings(RepeatSettings repeatSettings) {
        this.repeatSettings = repeatSettings;
    }

    public List<EquipmentStandardRelationDTO> getEquipmentStandardRelations() {
        return equipmentStandardRelations;
    }

    public void setEquipmentStandardRelations(List<EquipmentStandardRelationDTO> equipmentStandardRelations) {
        this.equipmentStandardRelations = equipmentStandardRelations;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
