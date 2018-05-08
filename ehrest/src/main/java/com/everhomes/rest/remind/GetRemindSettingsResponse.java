package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>remindTypes: 提醒选项，参考{@link com.everhomes.rest.remind.RemindSettingDTO}</li>
 * </ul>
 */
public class GetRemindSettingsResponse {
    private List<RemindSettingDTO> remindTypes;

    public List<RemindSettingDTO> getRemindTypes() {
        return remindTypes;
    }

    public void setRemindTypes(List<RemindSettingDTO> remindTypes) {
        this.remindTypes = remindTypes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
