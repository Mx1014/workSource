package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>beginDate: 开始时间</li>
 *     <li>endDate: 结束时间</li>
 *     <li>turnoverMinimum: 营业额最低值</li>
 *     <li>turnoverMaximum: 营业额最高值</li>
 *     <li>taxPaymentMinimum: 纳税额最低值</li>
 *     <li>taxPaymentMaximum: 纳税额最高值</li>
 *     <li>exportItems: 导出items列表</li>
 *     <li>orgId: organizationId</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ListEnterpriseCustomerStatisticsCommand {
    private Integer namespaceId;

    private Long communityId;

    private Long beginDate;

    private Long endDate;

    private Long orgId;

    private String exportItems;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

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

    public String getExportItems() {
        return exportItems;
    }

    public void setExportItems(String exportItems) {
        this.exportItems = exportItems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
