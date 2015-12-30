// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 */
/**
 * 
 * @author elians
 *<ul>
 *<li>namespaceId:命名空间</li>
 *<li>subject:主题</li>
 *<li>description:描述</li>
 *<li>location:位置</li>
 *<li>contactPerson:联系人</li>
 *<li>startTime:开始时间，时间格式为:YYYY-MM-DD hh:mm:ss</li>
 *<li>endTime:结束时间,时间格式为:YYYY-MM-DD hh:mm:ss</li>
 *<li>checkInFlag:签到标签，0-不需要签到、1-需要签到</li>
 *<li>confirmFlag:确认标签，0-不需要确认、1-需要确认</li>
 *<li>maxAttendeeCount:最大邀请人数</li>
 *<li>creatorFamilyId:创建者家庭ID</li>
 *<li>groupId:组ID</li>
 *<li>longitude:经度</li>
 *<li>latitude:纬度</li>
 *<li>changeVersion:版本</li>
 *<li>posterUri:海报</li>
 *<li>guest:嘉宾</li>
 *</ul>
 */
public class ActivityPostCommand{
    private Integer namespaceId;
    private String subject;
    private String description;
    private String location;
    private String contactPerson;
    private String contactNumber;
    private String startTime;
    private String endTime;
    private Byte checkinFlag;
    private Byte confirmFlag;
    private Integer maxAttendeeCount;
    private Long creatorFamilyId;
    private Long groupId;
    private Integer changeVersion;
    private Double longitude;
    private Double latitude;
    private String tag;
    private transient Long id;
    private String posterUri;
    private String guest;
    
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

    public Byte getCheckinFlag() {
        return checkinFlag;
    }

    public void setCheckinFlag(Byte checkinFlag) {
        this.checkinFlag = checkinFlag;
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

    public Integer getChangeVersion() {
        return changeVersion;
    }

    public void setChangeVersion(Integer changeVersion) {
        this.changeVersion = changeVersion;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
