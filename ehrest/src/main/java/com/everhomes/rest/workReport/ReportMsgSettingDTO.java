package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>msgTimeType: 时间类型 0-current 1-next 参考{@link com.everhomes.rest.workReport.ReportTimeType}</li>
 * <li>msgTimeMark: 时间标识 (周一,28号)</li>
 * <li>msgTime: 具体时间(09:30)</li>
 * </ul>
 */
public class ReportMsgSettingDTO {

    private Byte msgTimeType;

    private String msgTimeMark;

    private String msgTime;

    public ReportMsgSettingDTO() {
    }

    public Byte getMsgTimeType() {
        return msgTimeType;
    }

    public void setMsgTimeType(Byte msgTimeType) {
        this.msgTimeType = msgTimeType;
    }

    public String getMsgTimeMark() {
        return msgTimeMark;
    }

    public void setMsgTimeMark(String msgTimeMark) {
        this.msgTimeMark = msgTimeMark;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
