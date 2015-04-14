// @formatter:off
package com.everhomes.activity;

import com.everhomes.util.StringHelper;

public class ActivityDTO {
    private Long activityId;
    private Long startTime;
    private Long stopTime;
    private String location;
    private Integer signupFlag;
    private Integer confirmFlag;
    private Integer enrollUserCount;
    private Integer enrollFamilyCount;
    private Integer checkinUserCount;
    private Integer checkinFamilyCount;
    private Integer confirmUserCount;
    private Integer confirmFamilyCount;
   
    private Integer userActivityStatus;
    private Integer processStatus;
    
    public ActivityDTO() {
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
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

    public Integer getEnrollUserCount() {
        return enrollUserCount;
    }

    public void setEnrollUserCount(Integer enrollUserCount) {
        this.enrollUserCount = enrollUserCount;
    }

    public Integer getEnrollFamilyCount() {
        return enrollFamilyCount;
    }

    public void setEnrollFamilyCount(Integer enrollFamilyCount) {
        this.enrollFamilyCount = enrollFamilyCount;
    }

    public Integer getCheckinUserCount() {
        return checkinUserCount;
    }

    public void setCheckinUserCount(Integer checkinUserCount) {
        this.checkinUserCount = checkinUserCount;
    }

    public Integer getCheckinFamilyCount() {
        return checkinFamilyCount;
    }

    public void setCheckinFamilyCount(Integer checkinFamilyCount) {
        this.checkinFamilyCount = checkinFamilyCount;
    }

    public Integer getConfirmUserCount() {
        return confirmUserCount;
    }

    public void setConfirmUserCount(Integer confirmUserCount) {
        this.confirmUserCount = confirmUserCount;
    }

    public Integer getConfirmFamilyCount() {
        return confirmFamilyCount;
    }

    public void setConfirmFamilyCount(Integer confirmFamilyCount) {
        this.confirmFamilyCount = confirmFamilyCount;
    }

    public Integer getUserActivityStatus() {
        return userActivityStatus;
    }

    public void setUserActivityStatus(Integer userActivityStatus) {
        this.userActivityStatus = userActivityStatus;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
