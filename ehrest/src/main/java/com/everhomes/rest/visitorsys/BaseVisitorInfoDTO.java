// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link  com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>id: (必填)访客/预约ID</li>
 * <li>visitorName: (必填)访客姓名</li>
 * <li>followUpNumbers: (选填)随访人数</li>
 * <li>visitorPhone: (必填)访客电话</li>
 * <li>visitReasonId: (必填)随访事由id</li>
 * <li>visitReason: (必填)随访事由</li>
 * <li>inviterId: (选填)邀请者id,预约访客必填</li>
 * <li>inviterName: (选填)邀请者姓名,预约访客必填</li>
 * <li>plannedVisitTime: (必填)计划到访时间</li>
 * <li>visitTime: (选填)到访时间/登记时间</li>
 * <li>visitStatus: (必填)到访状态列表，{@link com.everhomes.rest.visitorsys.VisitorsysVisitStatus}</li>
 * <li>visitorType: (必填)访客类型，{@link com.everhomes.rest.visitorsys.VisitorsysVisitorType}</li>
 * <li>enterpriseId: (选填)公司id，园区访客必填</li>
 * <li>enterpriseName: (选填)公司名称，园区访客必填</li>
 * <li>officeLocationId: (选填)办公地点id,公司访客必填</li>
 * <li>officeLocationName: (选填)办公地点,公司访客必填</li>
 * <li>invalidTime: (根据配置选填/必填)有效期</li>
 * <li>plateNo: (根据配置选填/必填)车牌号</li>
 * <li>idNumber: (根据配置选填/必填)证件号码</li>
 * <li>visitFloor: (根据配置选填/必填)到访楼层</li>
 * <li>visitAddresses: (根据配置选填/必填)到访门牌</li>
 * <li>createTime: 创建时间</li>
 * <li>invitationNo: 预约编号</li>
 * </ul>
 */
public class BaseVisitorInfoDTO extends BaseVisitorDTO{
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

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
    private Byte visitorType;

    private Long enterpriseId;
    private String enterpriseName;

    private Long officeLocationId;
    private String officeLocationName;

    private Timestamp invalidTime;
    private String plateNo;
    private String idNumber;
    private String visitFloor;
    private String visitAddresses;

    private Timestamp createTime;
    private String invitationNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Timestamp getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Timestamp invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getVisitFloor() {
        return visitFloor;
    }

    public void setVisitFloor(String visitFloor) {
        this.visitFloor = visitFloor;
    }

    public String getVisitAddresses() {
        return visitAddresses;
    }

    public void setVisitAddresses(String visitAddresses) {
        this.visitAddresses = visitAddresses;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getInvitationNo() {
        return invitationNo;
    }

    public void setInvitationNo(String invitationNo) {
        this.invitationNo = invitationNo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
