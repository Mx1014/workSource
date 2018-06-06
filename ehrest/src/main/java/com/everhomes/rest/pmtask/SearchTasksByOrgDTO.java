//@formatter:off
package com.everhomes.rest.pmtask;

import java.sql.Timestamp;

/**
 * Created by Wentian Wang on 2018/6/6.
 */

public class SearchTasksByOrgDTO {
    private String taskCategoryName;
    private Long organizationUid;
    private String buildingName;
    private String requestorName;
    private String requestorPhone;
    private Timestamp createTime;
    private Byte status;

    public String getTaskCategoryName() {
        return taskCategoryName;
    }

    public void setTaskCategoryName(String taskCategoryName) {
        this.taskCategoryName = taskCategoryName;
    }

    public Long getOrganizationUid() {
        return organizationUid;
    }

    public void setOrganizationUid(Long organizationUid) {
        this.organizationUid = organizationUid;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
