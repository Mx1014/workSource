package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>startType: 开始类型 0-current 1-next 参考{@link com.everhomes.rest.workReport.WorkReportTimeType}</li>
 * <li>startMark: 开始标识 (周一,28号)</li>
 * <li>startTime: 开始时间(09:30)</li>
 * <li>endType: 结束类型 参考{@link com.everhomes.rest.workReport.WorkReportTimeType}</li>
 * <li>endMark: 结束标识</li>
 * <li>endTime: 结束时间</li>
 * </ul>
 */
public class WorkReportValiditySettings {

    private Byte startType;

    private String startMark;

    private String startTime;

    private Byte endType;

    private String endMark;

    private String endTime;

    public WorkReportValiditySettings() {
    }

    public Byte getStartType() {
        return startType;
    }

    public void setStartType(Byte startType) {
        this.startType = startType;
    }

    public String getStartMark() {
        return startMark;
    }

    public void setStartMark(String startMark) {
        this.startMark = startMark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Byte getEndType() {
        return endType;
    }

    public void setEndType(Byte endType) {
        this.endType = endType;
    }

    public String getEndMark() {
        return endMark;
    }

    public void setEndMark(String endMark) {
        this.endMark = endMark;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
