package com.everhomes.server.schema.tables.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class EhSyncTrans implements Serializable {
    private static final long serialVersionUID = -939979792L;
    private Long id;
    private Long syncAppId;
    private Long parentTransId;
    private Long versionId;
    private String content;
    private String result;
    private Byte state;
    private Timestamp lastUpdate;
    private Timestamp createTime;

    public EhSyncTrans() {
    }

    public EhSyncTrans(Long id, Long syncAppId, Long parentTransId, Long versionId, String content, String result,
            Byte state, Timestamp lastUpdate, Timestamp createTime) {
        this.id = id;
        this.syncAppId = syncAppId;
        this.parentTransId = parentTransId;
        this.versionId = versionId;
        this.content = content;
        this.result = result;
        this.state = state;
        this.lastUpdate = lastUpdate;
        this.createTime = createTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSyncAppId() {
        return this.syncAppId;
    }

    public void setSyncAppId(Long syncAppId) {
        this.syncAppId = syncAppId;
    }

    public Long getParentTransId() {
        return this.parentTransId;
    }

    public void setParentTransId(Long parentTransId) {
        this.parentTransId = parentTransId;
    }

    public Long getVersionId() {
        return this.versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Byte getState() {
        return this.state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Timestamp getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
