package com.everhomes.equipment;

import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.equipment.EquipmentStandardRelationDTO;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionPlans;
import com.everhomes.util.StringHelper;

import java.util.List;



public class EquipmentInspectionPlans  extends EhEquipmentInspectionPlans{

    private static final long serialVersionUID = -5939045682826070601L;

    private RepeatSettings repeatSettings;

    //增加执行频率 等
    private String executeStartTime;

    private String  executionFrequency;

    private String limitTime;

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

    public String getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(String executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    public String getExecutionFrequency() {
        return executionFrequency;
    }

    public void setExecutionFrequency(String executionFrequency) {
        this.executionFrequency = executionFrequency;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
