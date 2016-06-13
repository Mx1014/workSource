package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class ScheduleTaskDTO {
    private Byte     status;
    private String     progressData;
    private String     resourceType;
    private Long     resourceId;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Integer     progress;
    private Integer     processCount;
    private Long     id;
    

    public Byte getStatus() {
        return status;
    }



    public void setStatus(Byte status) {
        this.status = status;
    }



    public String getProgressData() {
        return progressData;
    }



    public void setProgressData(String progressData) {
        this.progressData = progressData;
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



    public Integer getProgress() {
        return progress;
    }



    public void setProgress(Integer progress) {
        this.progress = progress;
    }



    public Integer getProcessCount() {
        return processCount;
    }



    public void setProcessCount(Integer processCount) {
        this.processCount = processCount;
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
