package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name：类型名:如事假,病假,产假</li>
 * <li>timeCount：时间计数，单位天</li>
 * <li>timeCountDisplay: timeCount的格式化显示，如3.0天3.0小时</li>
 * </ul>
 */
public class ExtDTO {
    private String name;
    private String timeCount;
    private String timeCountDisplay;

    public ExtDTO() {
    }

    public ExtDTO(String name, String timeCount) {
        this.name = name;
        this.timeCount = timeCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(String timeCount) {
        this.timeCount = timeCount;
    }

    public String getTimeCountDisplay() {
        return timeCountDisplay;
    }

    public void setTimeCountDisplay(String timeCountDisplay) {
        this.timeCountDisplay = timeCountDisplay;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
