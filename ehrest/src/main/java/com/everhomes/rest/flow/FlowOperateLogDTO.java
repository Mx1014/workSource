package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>flowCaseId: flowCaseId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>flowCaseTitle: 标题</li>
 *     <li>flowUserName: 操作人</li>
 *     <li>flowCaseContent: 任务信息</li>
 *     <li>logContent: 日志内容</li>
 *     <li>createTime: 创建时间</li>
 *     <li>routeUri: 路由</li>
 * </ul>
 */
public class FlowOperateLogDTO {

    private Long id;
    private Long flowCaseId;
    private Long moduleId;
    private String flowCaseTitle;
    private String flowUserName;
    private String flowCaseContent;
    private String logContent;
    private Timestamp createTime;
    private String routeUri;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public String getFlowCaseTitle() {
        return flowCaseTitle;
    }

    public void setFlowCaseTitle(String flowCaseTitle) {
        this.flowCaseTitle = flowCaseTitle;
    }

    public String getFlowCaseContent() {
        return flowCaseContent;
    }

    public void setFlowCaseContent(String flowCaseContent) {
        this.flowCaseContent = flowCaseContent;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getFlowUserName() {
        return flowUserName;
    }

    public void setFlowUserName(String flowUserName) {
        this.flowUserName = flowUserName;
    }

    public String getRouteUri() {
        return routeUri;
    }

    public void setRouteUri(String routeUri) {
        this.routeUri = routeUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

