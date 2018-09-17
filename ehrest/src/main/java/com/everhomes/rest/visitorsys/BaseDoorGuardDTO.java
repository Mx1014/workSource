// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorGuardId : (必填)门禁id</li>
 * <li>doorGuardName : (必填)门禁名称</li>
 * <li>hardwareId : (必填)门禁硬件id</li>
 *
 * <li>maxDuration:访客授权最长有效期(天)</li>
 * <li>maxCount:访客授权最大次数</li>
 * <li>enableAmount:是否允许按次授权</li>
 * <li>enableDuration:是否允许按有效期授权</li>
 * </ul>
 */
public class BaseDoorGuardDTO {
    private String doorGuardId;
    private String doorGuardName;
    private String hardwareId;

    private Integer maxDuration;
    private Integer maxCount;

    private Byte enableAmount;
    private Byte enableDuration;

    public String getDoorGuardId() {
        return doorGuardId;
    }

    public void setDoorGuardId(String doorGuardId) {
        this.doorGuardId = doorGuardId;
    }

    public String getDoorGuardName() {
        return doorGuardName;
    }

    public void setDoorGuardName(String doorGuardName) {
        this.doorGuardName = doorGuardName;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Byte getEnableAmount() {
        return enableAmount;
    }

    public void setEnableAmount(Byte enableAmount) {
        this.enableAmount = enableAmount;
    }

    public Byte getEnableDuration() {
        return enableDuration;
    }

    public void setEnableDuration(Byte enableDuration) {
        this.enableDuration = enableDuration;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
