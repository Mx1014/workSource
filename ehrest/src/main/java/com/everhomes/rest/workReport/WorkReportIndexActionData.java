package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>displayName: 列表名称</li>
 * <li>appId: 空值</li>
 * <li>organizationId: 公司企业id</li>
 * <li>tabIndex: 0-写汇报列表 1-我接收的列表 2-我提交的列表</li>
 * </ul>
 */
public class WorkReportIndexActionData {
    private String displayName;

    private Long appId;

    private Long organizationId;

    private Long tabIndex;

    public WorkReportIndexActionData() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Long tabIndex) {
        this.tabIndex = tabIndex;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
