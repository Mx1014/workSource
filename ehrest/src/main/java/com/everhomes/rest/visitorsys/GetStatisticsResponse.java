// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>invitedVisitorCount: (选填)总访客预约数量</li>
 * <li>visitorCount: (选填)总访客数量</li>
 * <li>timeVisitorCount: (选填)一段时间的访客量</li>
 * <li>visitorCountStatsList: (选填)一段时间访客统计，{@link com.everhomes.rest.visitorsys.BaseVisitorStatsDTO}</li>
 * <li>dailyAvgStatsList: (选填)日平均访客统计，{@link com.everhomes.rest.visitorsys.BaseVisitorStatsDTO}</li>
 * <li>timeShareAvgStatsList: (选填)平均分时访客统计，{@link com.everhomes.rest.visitorsys.BaseVisitorStatsDTO}</li>
 * <li>enterpriseRankingList: (选填)最受欢迎企业统计，{@link com.everhomes.rest.visitorsys.VisitorMostVisitedDTO}</li>
 * <li>nextRankingPageAnchor: (选填)最受欢迎企业统计分页锚点</li>
 * </ul>
 */
public class GetStatisticsResponse {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

    private Long invitedVisitorCount;
    private Long visitorCount;

    private Long timeVisitorCount;
    @ItemType(BaseVisitorStatsDTO.class)
    private List<BaseVisitorStatsDTO> visitorCountStatsList;

    @ItemType(BaseVisitorStatsDTO.class)
    private List<BaseVisitorStatsDTO> dailyAvgStatsList;

    @ItemType(BaseVisitorStatsDTO.class)
    private List<BaseVisitorStatsDTO> timeShareAvgStatsList;

    @ItemType(VisitorMostVisitedDTO.class)
    private List<VisitorMostVisitedDTO> enterpriseRankingList;
    private Long nextRankingPageAnchor;

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

    public Long getInvitedVisitorCount() {
        return invitedVisitorCount;
    }

    public void setInvitedVisitorCount(Long invitedVisitorCount) {
        this.invitedVisitorCount = invitedVisitorCount;
    }

    public Long getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(Long visitorCount) {
        this.visitorCount = visitorCount;
    }

    public Long getTimeVisitorCount() {
        return timeVisitorCount;
    }

    public void setTimeVisitorCount(Long timeVisitorCount) {
        this.timeVisitorCount = timeVisitorCount;
    }

    public List<BaseVisitorStatsDTO> getVisitorCountStatsList() {
        return visitorCountStatsList;
    }

    public void setVisitorCountStatsList(List<BaseVisitorStatsDTO> visitorCountStatsList) {
        this.visitorCountStatsList = visitorCountStatsList;
    }

    public List<BaseVisitorStatsDTO> getDailyAvgStatsList() {
        return dailyAvgStatsList;
    }

    public void setDailyAvgStatsList(List<BaseVisitorStatsDTO> dailyAvgStatsList) {
        this.dailyAvgStatsList = dailyAvgStatsList;
    }

    public List<BaseVisitorStatsDTO> getTimeShareAvgStatsList() {
        return timeShareAvgStatsList;
    }

    public void setTimeShareAvgStatsList(List<BaseVisitorStatsDTO> timeShareAvgStatsList) {
        this.timeShareAvgStatsList = timeShareAvgStatsList;
    }

    public List<VisitorMostVisitedDTO> getEnterpriseRankingList() {
        return enterpriseRankingList;
    }

    public void setEnterpriseRankingList(List<VisitorMostVisitedDTO> enterpriseRankingList) {
        this.enterpriseRankingList = enterpriseRankingList;
    }

    public Long getNextRankingPageAnchor() {
        return nextRankingPageAnchor;
    }

    public void setNextRankingPageAnchor(Long nextRankingPageAnchor) {
        this.nextRankingPageAnchor = nextRankingPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
