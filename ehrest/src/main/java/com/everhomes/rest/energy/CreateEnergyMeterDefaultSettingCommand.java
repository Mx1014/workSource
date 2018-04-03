package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>defaultSettings: 新增表记默认值 参考{@link com.everhomes.rest.energy.EnergyMeterDefaultSettingTemplateDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/3/22.
 */
public class CreateEnergyMeterDefaultSettingCommand {

    @ItemType(EnergyMeterDefaultSettingTemplateDTO.class)
    private List<EnergyMeterDefaultSettingTemplateDTO> defaultSettings;

    public List<EnergyMeterDefaultSettingTemplateDTO> getDefaultSettings() {
        return defaultSettings;
    }

    public void setDefaultSettings(List<EnergyMeterDefaultSettingTemplateDTO> defaultSettings) {
        this.defaultSettings = defaultSettings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
