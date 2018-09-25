// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.visitorsys.VisitorsysStatus;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>searchFlag: (必填)搜索标识，{@link com.everhomes.rest.visitorsys.VisitorsysSearchFlagType}</li>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>keyWords: (选填)访客信息,访客姓名手机号码模糊搜索</li>
 * <li>visitorType: (选填)访客类型，{@link com.everhomes.rest.visitorsys.VisitorsysVisitorType}</li>
 * <li>visitStatus: (选填)访客状态，{@link VisitorsysStatus}</li>
 * <li>bookingStatus: (选填)预约状态，{@link VisitorsysStatus}</li>
 * <li>startPlannedVisitTime: (选填)计划到访时间开始，预约管理用</li>
 * <li>endPlannedVisitTime: (选填)计划到访时间结束，预约管理用</li>
 * <li>officeLocationId: (选填)办公地点id，企业访客模块用</li>
 * <li>enterpriseId: (选填)公司id，园区访客模块用</li>
 * <li>visitReasonId: (选填)事由id</li>
 * <li>startVisitTime: (选填)实际到访企业/公司时间开始，访客管理用</li>
 * <li>endVisitTime: (选填)实际到访企业/公司时间结束，访客管理用</li>
 * <li>pageAnchor: (选填)锚点</li>
 * <li>pageSize: (选填)每页的数量</li>
 * <li>idNumber: (选填)证件号</li>
 * </ul>
 */
public class ListBookedVisitorParams{
    private Byte searchFlag;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String keyWords;
    private Byte visitorType;
    private Byte visitStatus;
    private Byte bookingStatus;
    private Long startPlannedVisitTime;
    private Long endPlannedVisitTime;
    private Long officeLocationId;
    private Long enterpriseId;
    private Long visitReasonId;
    private Long startVisitTime;
    private Long endVisitTime;
    private Long pageAnchor;
    private Integer pageSize;
    private String idNumber;

    //统计所用
    private Long startVisitorCountTime;
    private Long endVisitorCountTime;
    private Long startDailyAvgVisitorTime;
    private Long endDailyAvgVisitorTime;
    private Long startTimeShareAvgVisitorTime;
    private Long endTimeShareAvgVisitorTime;
    private Long startRankingTime;
    private Long endRankingTime;
    private Long rankingPageAnchor;
    private Integer rankingPageSize;
    private String visitorPhone;

    public Byte getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(Byte searchFlag) {
        this.searchFlag = searchFlag;
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

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public Byte getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(Byte visitorType) {
        this.visitorType = visitorType;
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

    public Long getStartPlannedVisitTime() {
        return startPlannedVisitTime;
    }

    public void setStartPlannedVisitTime(Long startPlannedVisitTime) {
        this.startPlannedVisitTime = startPlannedVisitTime;
    }

    public Long getEndPlannedVisitTime() {
        return endPlannedVisitTime;
    }

    public void setEndPlannedVisitTime(Long endPlannedVisitTime) {
        this.endPlannedVisitTime = endPlannedVisitTime;
    }

    public Long getOfficeLocationId() {
        return officeLocationId;
    }

    public void setOfficeLocationId(Long officeLocationId) {
        this.officeLocationId = officeLocationId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getVisitReasonId() {
        return visitReasonId;
    }

    public void setVisitReasonId(Long visitReasonId) {
        this.visitReasonId = visitReasonId;
    }

    public Long getStartVisitTime() {
        return startVisitTime;
    }

    public void setStartVisitTime(Long startVisitTime) {
        this.startVisitTime = startVisitTime;
    }

    public Long getEndVisitTime() {
        return endVisitTime;
    }

    public void setEndVisitTime(Long endVisitTime) {
        this.endVisitTime = endVisitTime;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getStartVisitorCountTime() {
        return startVisitorCountTime;
    }

    public void setStartVisitorCountTime(Long startVisitorCountTime) {
        this.startVisitorCountTime = startVisitorCountTime;
    }

    public Long getEndVisitorCountTime() {
        return endVisitorCountTime;
    }

    public void setEndVisitorCountTime(Long endVisitorCountTime) {
        this.endVisitorCountTime = endVisitorCountTime;
    }

    public Long getStartDailyAvgVisitorTime() {
        return startDailyAvgVisitorTime;
    }

    public void setStartDailyAvgVisitorTime(Long startDailyAvgVisitorTime) {
        this.startDailyAvgVisitorTime = startDailyAvgVisitorTime;
    }

    public Long getEndDailyAvgVisitorTime() {
        return endDailyAvgVisitorTime;
    }

    public void setEndDailyAvgVisitorTime(Long endDailyAvgVisitorTime) {
        this.endDailyAvgVisitorTime = endDailyAvgVisitorTime;
    }

    public Long getStartTimeShareAvgVisitorTime() {
        return startTimeShareAvgVisitorTime;
    }

    public void setStartTimeShareAvgVisitorTime(Long startTimeShareAvgVisitorTime) {
        this.startTimeShareAvgVisitorTime = startTimeShareAvgVisitorTime;
    }

    public Long getEndTimeShareAvgVisitorTime() {
        return endTimeShareAvgVisitorTime;
    }

    public void setEndTimeShareAvgVisitorTime(Long endTimeShareAvgVisitorTime) {
        this.endTimeShareAvgVisitorTime = endTimeShareAvgVisitorTime;
    }

    public Long getStartRankingTime() {
        return startRankingTime;
    }

    public void setStartRankingTime(Long startRankingTime) {
        this.startRankingTime = startRankingTime;
    }

    public Long getEndRankingTime() {
        return endRankingTime;
    }

    public void setEndRankingTime(Long endRankingTime) {
        this.endRankingTime = endRankingTime;
    }

    public Long getRankingPageAnchor() {
        return rankingPageAnchor;
    }

    public void setRankingPageAnchor(Long rankingPageAnchor) {
        this.rankingPageAnchor = rankingPageAnchor;
    }

    public Integer getRankingPageSize() {
        return rankingPageSize;
    }

    public void setRankingPageSize(Integer rankingPageSize) {
        this.rankingPageSize = rankingPageSize;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
