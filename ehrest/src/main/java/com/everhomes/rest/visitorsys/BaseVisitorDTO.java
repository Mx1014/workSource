// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: (必填)id</li>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
 * <li>visitorName: (必填)访客姓名</li>
 * <li>followUpNumbers: (选填)随访人数</li>
 * <li>visitorPhone: (必填)访客电话</li>
 * <li>visitReasonId: (必填)随访事由id</li>
 * <li>visitReason: (必填)随访事由</li>
 * <li>inviterId: (选填)邀请者id,预约访客必填</li>
 * <li>inviterName: (选填)邀请者姓名,预约访客必填</li>
 * <li>plannedVisitTime: (必填)计划到访时间</li>
 * <li>visitTime: (选填)到访时间/登记时间</li>
 * <li>visitStatus: (必填)访客状态列表，{@link com.everhomes.rest.visitorsys.VisitorsysStatus}</li>
 * <li>bookingStatus: (必填)预约状态列表，{@link com.everhomes.rest.visitorsys.VisitorsysStatus}</li>
 * <li>visitorType: (必填)访客类型，{@link com.everhomes.rest.visitorsys.VisitorsysVisitorType}</li>
 * <li>enterpriseId: (选填)公司id，园区访客必填</li>
 * <li>enterpriseName: (选填)公司名称，园区访客必填</li>
 * <li>officeLocationId: (选填)办公地点id,公司访客必填</li>
 * <li>officeLocationName: (选填)办公地点,公司访客必填</li>
 * <li>doorAccessAuthDurationType: 访客授权有效期种类,0 天数，1 小时数</li>
 * <li>doorAccessAuthDuration: 访客授权有效期</li>
 * <li>doorAccessEnableAuthCount: 访客授权次数开关 0 关 1 开</li>
 * <li>doorAccessAuthCount: 访客授权次数</li>
 * </ul>
 */
public class BaseVisitorDTO extends BaseVisitorsysCommand{
    private Long id;
    private String visitorName;
    private Long followUpNumbers;
    private String visitorPhone;

    private Long visitReasonId;
    private String visitReason;

    private Long inviterId;
    private String inviterName;

    private Timestamp plannedVisitTime;
    private Timestamp visitTime;

    private Byte visitStatus;
    private Byte bookingStatus;
    private Byte visitorType;

    private Long enterpriseId;
    private String enterpriseName;

    private Long officeLocationId;
    private String officeLocationName;
    private Timestamp createTime;

    private Byte doorAccessAuthDurationType;
    private Integer doorAccessAuthDuration;
    private Byte doorAccessEnableAuthCount;
    private Integer doorAccessAuthCount;

    private String idNumber;

    private Long doorAccessEndTime;

    private Byte communityType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public Long getFollowUpNumbers() {
        return followUpNumbers;
    }

    public void setFollowUpNumbers(Long followUpNumbers) {
        this.followUpNumbers = followUpNumbers;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public Long getVisitReasonId() {
        return visitReasonId;
    }

    public void setVisitReasonId(Long visitReasonId) {
        this.visitReasonId = visitReasonId;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public Long getInviterId() {
        return inviterId;
    }

    public void setInviterId(Long inviterId) {
        this.inviterId = inviterId;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }

    public Timestamp getPlannedVisitTime() {
        return plannedVisitTime;
    }

    public void setPlannedVisitTime(Timestamp plannedVisitTime) {
        this.plannedVisitTime = plannedVisitTime;
    }

    public Timestamp getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Timestamp visitTime) {
        this.visitTime = visitTime;
    }

    public Byte getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(Byte visitStatus) {
        this.visitStatus = visitStatus;
    }

    public Byte getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Byte bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Byte getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(Byte visitorType) {
        this.visitorType = visitorType;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Long getOfficeLocationId() {
        return officeLocationId;
    }

    public void setOfficeLocationId(Long officeLocationId) {
        this.officeLocationId = officeLocationId;
    }

    public String getOfficeLocationName() {
        return officeLocationName;
    }

    public void setOfficeLocationName(String officeLocationName) {
        this.officeLocationName = officeLocationName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getDoorAccessAuthDurationType() {
        return doorAccessAuthDurationType;
    }

    public void setDoorAccessAuthDurationType(Byte doorAccessAuthDurationType) {
        this.doorAccessAuthDurationType = doorAccessAuthDurationType;
    }

    public Integer getDoorAccessAuthDuration() {
        return doorAccessAuthDuration;
    }

    public void setDoorAccessAuthDuration(Integer doorAccessAuthDuration) {
        this.doorAccessAuthDuration = doorAccessAuthDuration;
    }

    public Byte getDoorAccessEnableAuthCount() {
        return doorAccessEnableAuthCount;
    }

    public void setDoorAccessEnableAuthCount(Byte doorAccessEnableAuthCount) {
        this.doorAccessEnableAuthCount = doorAccessEnableAuthCount;
    }

    public Integer getDoorAccessAuthCount() {
        return doorAccessAuthCount;
    }

    public void setDoorAccessAuthCount(Integer doorAccessAuthCount) {
        this.doorAccessAuthCount = doorAccessAuthCount;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Long getDoorAccessEndTime() {
        return doorAccessEndTime;
    }

    public void setDoorAccessEndTime(Long doorAccessEndTime) {
        this.doorAccessEndTime = doorAccessEndTime;
    }

    @Override
    public Byte getCommunityType() {
        return communityType;
    }

    @Override
    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
