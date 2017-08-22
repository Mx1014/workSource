package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>beginDate: 开始时间</li>
 *     <li>endDate: 结束时间</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ListEnterpriseCustomerStatisticsCommand {
    private Integer namespaceId;

    private Long communityId;

    private Long beginDate;

    private Long endDate;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
}
