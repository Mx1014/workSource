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
public class GetConfigurationResponse {
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

    private Long configVersion;
    private String guideInfo;
    private String selfRegisterQrcodeUri;
    private String ipadCoverPlanUri;
    private String ipadThemeRgb;
    private String secrecyAgreement;
    private String welcomePages;
    private String configJson;
    private String configFormJson;
    private String configPassCardJson;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
