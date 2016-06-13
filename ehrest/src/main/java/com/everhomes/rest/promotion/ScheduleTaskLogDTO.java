package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class ScheduleTaskLogDTO {
    private Timestamp     startTime;
    private String     resourceType;
    private Long     resourceId;
    private String     ownerType;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Long     taskId;
    private String     resultData;
    private Long     ownerId;
    private Timestamp     endTime;
    private Long     id;


    public Timestamp getStartTime() {
        return startTime;
    }


    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }


    public String getResourceType() {
        return resourceType;
    }


    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public Long getResourceId() {
        return resourceId;
    }


    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }


    public String getOwnerType() {
        return ownerType;
    }


    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public Integer getNamespaceId() {
        return namespaceId;
    }


    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public Long getTaskId() {
        return taskId;
    }


    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


    public String getResultData() {
        return resultData;
    }


    public void setResultData(String resultData) {
        this.resultData = resultData;
    }


    public Long getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Timestamp getEndTime() {
        return endTime;
    }


    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
