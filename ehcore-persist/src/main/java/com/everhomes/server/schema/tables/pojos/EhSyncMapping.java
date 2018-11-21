package com.everhomes.server.schema.tables.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class EhSyncMapping implements Serializable {
    private static final long serialVersionUID = -177419982L;
    private Long id;
    private Long syncAppId;
    private String name;
    private String content;
    private Timestamp createTime;
    private Byte status;

    public EhSyncMapping() {
    }

    public EhSyncMapping(Long id, Long syncAppId, String name, String content, Timestamp createTime, Byte status) {
        this.id = id;
        this.syncAppId = syncAppId;
        this.name = name;
        this.content = content;
        this.createTime = createTime;
        this.status = status;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
