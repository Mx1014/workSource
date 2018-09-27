package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;
import java.sql.Timestamp;

public class LaunchPadIndexDTO {
    private Byte     status;
    private Timestamp     updateTime;
    private Long     operatorUid;
    private String     name;
    private Long     creatorUid;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private String     configJson;
    private String     iconUri;
    private String     selectedIconUri;
    private String     type;
    private Long     id;
    private String     description;


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


    public Long getOperatorUid() {
        return operatorUid;
    }


    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getCreatorUid() {
        return creatorUid;
    }


    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
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


    public String getConfigJson() {
        return configJson;
    }


    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }


    public String getIconUri() {
        return iconUri;
    }


    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }


    public String getSelectedIconUri() {
        return selectedIconUri;
    }


    public void setSelectedIconUri(String selectedIconUri) {
        this.selectedIconUri = selectedIconUri;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
