package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工detailId</li>
 * <li>inOutType: 增减类型：0-社保, 1-公积金</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * </ul>
 */
public class AddSocialSecurityInOutTimeCommand {

    private Long detailId;

    private Byte inOutType;

    private String startTime;

    private String endTime;

    public AddSocialSecurityInOutTimeCommand() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Byte getInOutType() {
        return inOutType;
    }

    public void setInOutType(Byte inOutType) {
        this.inOutType = inOutType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
