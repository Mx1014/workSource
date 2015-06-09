// @formatter:off
package com.everhomes.activity;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>activityId:活动ID</li>
 *<li>startTime:开始时间<li>
 *<li>stopTime:结束时间</li>
 *<li>location:位置</li>
 *<li>signupFlag:报名标签</li>
 *<li>confirmFlag:确认</li>
 *<li>enrollUserCount:报名人数</li>
 *<li>enrollFamilyCount:报名家庭数</li>
 *<li>checkinUserCount:签到人数</li>
 *<li>checkinFamilyCount:签到家庭数</li>
 *<li>confirmUserCount:确认人数</li>
 *<li>confirmFamilyCount：确认家庭数</li>
 *<li>familyId:家庭ID</li>
 *<li>groupId:圈ID</li>
 *<li>userActivityStatus:活动状态</li>
 *<li>processStatus：处理状态</li>
 *</ul>
 */
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
    private Long groupId;
    private Long familyId;
   
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
    
    
    
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
