package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * Created by Rui.Jia  2018/7/2 16 :27
 */

public class PotentialCustomerDTO {
    private Long id;
    private Integer namespaceId;
    private String name;
    private Long sourceId;
    private String sourceType;
    private String originSourceName;
    private Long operateUid;
    private Timestamp updateTime;
    private Timestamp createTime;
    private Long deleteUid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }


    public Long getOperateUid() {
        return operateUid;
    }

    public void setOperateUid(Long operateUid) {
        this.operateUid = operateUid;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getDeleteUid() {
        return deleteUid;
    }

    public void setDeleteUid(Long deleteUid) {
        this.deleteUid = deleteUid;
    }

    public String getOriginSourceName() {
        return originSourceName;
    }

    public void setOriginSourceName(String originSourceName) {
        this.originSourceName = originSourceName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
