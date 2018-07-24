package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>repeatRemindDTO: 重复日程创建的新日程</li>
 * <li>originRemindDTO: 原日程</li>
 * </ul>
 */
public class UpdateRemindStatusResponse {
    private RemindDTO repeatRemindDTO;
    private RemindDTO originRemindDTO;

    public RemindDTO getRepeatRemindDTO() {
        return repeatRemindDTO;
    }

    public void setRepeatRemindDTO(RemindDTO repeatRemindDTO) {
        this.repeatRemindDTO = repeatRemindDTO;
    }

    public RemindDTO getOriginRemindDTO() {
        return originRemindDTO;
    }

    public void setOriginRemindDTO(RemindDTO originRemindDTO) {
        this.originRemindDTO = originRemindDTO;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
