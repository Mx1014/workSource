// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>statsAllTimeCountFlag: (必填)是否返回所有的访客预约和访客量，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * <li>startVisitorCountTime: (选填)访客量开始时间</li>
 * <li>endVisitorCountTime: (选填)访客量结束时间</li>
 * <li>startDailyAvgVisitorTime: (选填)日平均访客开始时间</li>
 * <li>endDailyAvgVisitorTime: (选填)日平均访客结束时间</li>
 * <li>startTimeShareAvgVisitorTime: (选填)分时平均访客开始时间</li>
 * <li>endTimeShareAvgVisitorTime: (选填)分时平均访客结束时间</li>
 * <li>startRankingTime: (选填)最常被访问的开始时间，园区访客用</li>
 * <li>endRankingTime: (选填)最常被访问的结束时间，园区访客用</li>
 * <li>rankingPageAnchor: (选填)锚点</li>
 * <li>rankingPageSize: (选填)每页的数量</li>
 * </ul>
 */
public class GetStatisticsCommand extends BaseVisitorsysCommand{
    private Byte statsAllTimeCountFlag;
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

    public Byte getStatsAllTimeCountFlag() {
        return statsAllTimeCountFlag;
    }

    public void setStatsAllTimeCountFlag(Byte statsAllTimeCountFlag) {
        this.statsAllTimeCountFlag = statsAllTimeCountFlag;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
