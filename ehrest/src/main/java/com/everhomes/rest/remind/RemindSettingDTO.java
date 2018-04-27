package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>remindTypeId: 提醒类型ID</li>
 * <li>remindDisplayName: 提醒显示的文本</li>
 * <li>offsetDay: 提前几天</li>
 * <li>fixTime: 提醒时间:09:00的时间戳</li>
 * <li>defaultOrder: 排序</li>
 * </ul>
 */
public class RemindSettingDTO {
    private Integer remindTypeId;
    private String remindDisplayName;
    private Byte offsetDay;
    private Long fixTime;
    private Integer defaultOrder;

    public Integer getRemindTypeId() {
        return remindTypeId;
    }

    public void setRemindTypeId(Integer remindTypeId) {
        this.remindTypeId = remindTypeId;
    }

    public String getRemindDisplayName() {
        return remindDisplayName;
    }

    public void setRemindDisplayName(String remindDisplayName) {
        this.remindDisplayName = remindDisplayName;
    }

    public Byte getOffsetDay() {
        return offsetDay;
    }

    public void setOffsetDay(Byte offsetDay) {
        this.offsetDay = offsetDay;
    }

    public Long getFixTime() {
        return fixTime;
    }

    public void setFixTime(Long fixTime) {
        this.fixTime = fixTime;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
