package com.everhomes.rest.general_approval;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * json 字符串,不同的字段属性有不同的 extra. 时间类型
 * <ul>
 *     <li>startTime: startTime</li>
 *     <li>endTime: endTime</li>
 * </ul>
 */
public class SearchGeneralFormFieldExtra {
    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SearchGeneralFormFieldExtra{" +
                "startTime=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime) +
                ", endTime=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime) +
                '}';
    }
}
