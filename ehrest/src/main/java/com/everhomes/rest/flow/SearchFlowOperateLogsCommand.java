package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>userId: 用户ID，为空则为当前用户</li>
 *     <li>flowCaseId: flowCaseId</li>
 *     <li>serviceType: 业务类别</li>
 *     <li>keyword: 搜索关键字</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class SearchFlowOperateLogsCommand {

    private Long userId;
    private Long flowCaseId;
    private String serviceType;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
