package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>userId: 用户ID，为空则为当前用户</li>
 *     <li>flowCaseId: flowCaseId</li>
 *     <li>organizationId: 公司id</li>
 *     <li>moduleId: 模块id</li>
 *     <li>serviceType: 业务类别</li>
 *     <li>keyword: 搜索关键字</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 *     <li>adminFlag: adminFlag</li>
 * </ul>
 */
public class SearchFlowOperateLogsCommand {

    private Long userId;
    private Long flowCaseId;
    private Long organizationId;
    private Long moduleId;
    private String serviceType;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;

    private Byte adminFlag;

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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Byte getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(Byte adminFlag) {
        this.adminFlag = adminFlag;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
