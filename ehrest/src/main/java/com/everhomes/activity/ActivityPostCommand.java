// @formatter:off
package com.everhomes.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 */
/**
 * 
 * @author elians
 *<ul>
 *<li>subject:主题</li>
 *<li>description:描述</li>
 *<li>location:位置</li>
 *<li>contactPerson:联系人</li>
 *<li>startTime:开始时间，时间格式为:YYYY-MM-DD hh:mm:ss</li>
 *<li>endTime:结束时间,时间格式为:YYYY-MM-DD hh:mm:ss</li>
 *<li>signupFlag:签到标签</li>
 *<li>confirmFlag:回复标签</li>
 *<li>maxAttendeeCount:最大邀请人数</li>
 *<li>creatorFamilyId:创建者家庭ID</li>
 *<li>groupId:组ID</li>
 *<li>status:状态</li>
 *<li>changeVersion:版本</li>
 *</ul>
 */
public class ActivityPostCommand{
    private String   subject;
    private String   description;
    private String   location;
    private String   contactPerson;
    private String contactNumber;
    private String startTime;
    private String endTime;
    private Byte     signupFlag;
    private Byte     confirmFlag;
    private Integer  maxAttendeeCount;
    private Long     creatorFamilyId;
    private Long     groupId;
    private Byte     status;
    private Integer  changeVersion;
    
    public ActivityPostCommand() {
    }

   

    public String getSubject() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public String getContactPerson() {
        return contactPerson;
    }


    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }





    public String getContactNumber() {
        return contactNumber;
    }


    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }




    public Byte getSignupFlag() {
        return signupFlag;
    }


    public void setSignupFlag(Byte signupFlag) {
        this.signupFlag = signupFlag;
    }


    public Byte getConfirmFlag() {
        return confirmFlag;
    }


    public void setConfirmFlag(Byte confirmFlag) {
        this.confirmFlag = confirmFlag;
    }


    public Integer getMaxAttendeeCount() {
        return maxAttendeeCount;
    }


    public void setMaxAttendeeCount(Integer maxAttendeeCount) {
        this.maxAttendeeCount = maxAttendeeCount;
    }




    public Long getCreatorFamilyId() {
        return creatorFamilyId;
    }


    public void setCreatorFamilyId(Long creatorFamilyId) {
        this.creatorFamilyId = creatorFamilyId;
    }



    public Long getGroupId() {
        return groupId;
    }


    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }


    public Integer getChangeVersion() {
        return changeVersion;
    }


    public void setChangeVersion(Integer changeVersion) {
        this.changeVersion = changeVersion;
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
