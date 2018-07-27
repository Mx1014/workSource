// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
  *<ul>
  *<li>visitorToken : 访客token</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>visitorName: (必填)访客姓名</li>
 * <li>identityCard: (必填)身份证</li>
 * <li>followUpNumbers: (选填)随访人数</li>
 * <li>visitorPhone: (必填)访客电话</li>
 * <li>visitReasonId: (必填)随访事由id</li>
 * <li>visitReason: (必填)随访事由</li>
 * <li>plannedVisitTime: (必填)计划到访时间</li>
 * <li>visitorType: (必填)访客类型，{@link com.everhomes.rest.visitorsys.VisitorsysVisitorType}</li>
 * <li>enterpriseId: (选填)公司id</li>
 * <li>enterpriseName: (选填)公司名称</li>
 * <li>buildings: (选填)楼栋列表</li>
  *</ul>
  */

public class OpenApiCreateVisitorCommand {
    private String visitorToken;
    private String ownerType;
    private Long ownerId;
    private String visitorName;
    private String identityCard;
    private String visitorPhone;
    private Long followUpNumbers;

    private Long visitReasonId;
    private String visitReason;

    private Timestamp plannedVisitTime;
    private Byte visitorType;

    private Long enterpriseId;
    private String enterpriseName;

    private List<VisitorSysBuilding> buildings;

    public String getVisitorToken() {
        return visitorToken;
    }

    public void setVisitorToken(String visitorToken) {
        this.visitorToken = visitorToken;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public Long getFollowUpNumbers() {
        return followUpNumbers;
    }

    public void setFollowUpNumbers(Long followUpNumbers) {
        this.followUpNumbers = followUpNumbers;
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

    public Timestamp getPlannedVisitTime() {
        return plannedVisitTime;
    }

    public void setPlannedVisitTime(Timestamp plannedVisitTime) {
        this.plannedVisitTime = plannedVisitTime;
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

    public List<VisitorSysBuilding> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<VisitorSysBuilding> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
