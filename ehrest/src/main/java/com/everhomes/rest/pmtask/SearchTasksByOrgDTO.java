//@formatter:off
package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * Created by Wentian Wang on 2018/6/6.
 */

public class SearchTasksByOrgDTO {
    private String taskCategoryName;
    private String pmTaskSource;
    private String buildingName;
    private String requestorName;
    private String requestorPhone;
    private Timestamp createTime;
    private String status;
    private String content;
    private Long flowCaseId;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTaskCategoryName() {
        return taskCategoryName;
    }

    public void setTaskCategoryName(String taskCategoryName) {
        this.taskCategoryName = taskCategoryName;
    }

    public String getPmTaskSource() {
        return pmTaskSource;
    }

    public void setPmTaskSource(String pmTaskSource) {
        this.pmTaskSource = pmTaskSource;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getRequestorPhone() {
        return requestorPhone;
    }

    public void setRequestorPhone(String requestorPhone) {
        this.requestorPhone = requestorPhone;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
