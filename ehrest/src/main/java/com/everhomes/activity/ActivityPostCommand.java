// @formatter:off
package com.everhomes.activity;

import com.everhomes.forum.NewTopicCommand;
import com.everhomes.util.StringHelper;

public class ActivityPostCommand extends NewTopicCommand {
    private Long startTime;
    private Long stopTime;
    private String location;
    private Integer signupFlag;
    private Integer confirmFlag;
    private Double longitude;
    private Double Latitude;
    
    public ActivityPostCommand() {
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = stopTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSignupFlag() {
        return signupFlag;
    }

    public void setSignupFlag(Integer signupFlag) {
        this.signupFlag = signupFlag;
    }

    public Integer getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
