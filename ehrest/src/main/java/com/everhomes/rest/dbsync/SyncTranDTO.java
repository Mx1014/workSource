package com.everhomes.rest.dbsync;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class SyncTranDTO {
    private String     content;
    private Timestamp     lastUpdate;
    private Long     syncAppId;
    private Byte     state;
    private Timestamp     createTime;
    private Long     parentTransId;
    private Long     versionId;
    private String     result;
    private Long     id;


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public Timestamp getLastUpdate() {
        return lastUpdate;
    }


    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    public Long getSyncAppId() {
        return syncAppId;
    }


    public void setSyncAppId(Long syncAppId) {
        this.syncAppId = syncAppId;
    }


    public Byte getState() {
        return state;
    }


    public void setState(Byte state) {
        this.state = state;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public Long getParentTransId() {
        return parentTransId;
    }


    public void setParentTransId(Long parentTransId) {
        this.parentTransId = parentTransId;
    }


    public Long getVersionId() {
        return versionId;
    }


    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }


    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
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
