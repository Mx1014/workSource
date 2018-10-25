package com.everhomes.rest.smartcard;

import com.everhomes.util.StringHelper;
import java.sql.Timestamp;

public class SmartCardKeyDTO {
    private Byte     status;
    private Timestamp     updateTime;
    private String     name;
    private Long     id;
    private Integer     namespaceId;
    private Timestamp     createTime;


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }


    public Timestamp getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


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


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

