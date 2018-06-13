// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>ranking : (必填)排名</li>
 *<li>enterpriseName : (必填)企业名称</li>
 *<li>visitorCount : (必填)访问者数量</li>
 *</ul>
 */
public class VisitorMostVisitedDTO{
    private Integer ranking;
    private String enterpriseName;
    private Long visitorCount;

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Long getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(Long visitorCount) {
        this.visitorCount = visitorCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
