package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>msgTimeType: 时间类型 0-current 1-next 参考{@link com.everhomes.rest.workReport.WorkReportTimeType}</li>
 * <li>msgTimeMark: 时间标识 (周一,28号)</li>
 * <li>msgTime: 具体时间(09:30)</li>
 * </ul>
 */
public class WorkReportMsgSettings {

    private Byte msgTimeType;

    private String msgTimeMark;

    private String msgTime;

    public Byte getMsgTimeType() {
        return msgTimeType;
    }

    public void setMsgTimeType(Byte msgTimeType) {
        this.msgTimeType = msgTimeType;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }
}
