// @formatter:off
package com.everhomes.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 */
public class ActivityPostCommand{
    private Integer  namespaceId;
    private String   subject;
    private String   description;
    private String   location;
    private String   contactPerson;
    private String   contactNumber;
    private Long     startTimeMs;
    private Long     endTimeMs;
    private Byte     signupFlag;
    private Byte     confirmFlag;
    private Integer  maxAttendeeCount;
    private Long     creatorUid;
    private Long     creatorFamilyId;
    private String   groupDiscriminator;
    private Long     groupId;
    private Byte     status;
    private Integer  changeVersion;
    private Long createTime;
    private Long deleteTime;
    
    public ActivityPostCommand() {
    }

   
    public Integer getNamespaceId() {
        return namespaceId;
    }


    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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


    public Long getStartTimeMs() {
        return startTimeMs;
    }


    public void setStartTimeMs(Long startTimeMs) {
        this.startTimeMs = startTimeMs;
    }


    public Long getEndTimeMs() {
        return endTimeMs;
    }


    public void setEndTimeMs(Long endTimeMs) {
        this.endTimeMs = endTimeMs;
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


    public Long getCreatorUid() {
        return creatorUid;
    }


    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }


    public Long getCreatorFamilyId() {
        return creatorFamilyId;
    }


    public void setCreatorFamilyId(Long creatorFamilyId) {
        this.creatorFamilyId = creatorFamilyId;
    }


    public String getGroupDiscriminator() {
        return groupDiscriminator;
    }


    public void setGroupDiscriminator(String groupDiscriminator) {
        this.groupDiscriminator = groupDiscriminator;
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


    public Long getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    public Long getDeleteTime() {
        return deleteTime;
    }


    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
