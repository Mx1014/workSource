package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>commitVersion: 版本号</li>
 * <li>createTime: createTime</li>
 * <li>publishTime: 发布时间</li>
 * <li>publishCode: 是否发布标记</li>
 * </ul>
 */
public class ScriptVersionInfoDTO {

    private String    id ;
    private Integer   namespaceId ;
    private String   infoType ;
    private String   commitVersion ;
    private Timestamp createTime ;
    private Timestamp   publishTime ;
    private Byte   publishCode ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getCommitVersion() {
        return commitVersion;
    }

    public void setCommitVersion(String commitVersion) {
        this.commitVersion = commitVersion;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    public Byte getPublishCode() {
        return publishCode;
    }

    public void setPublishCode(Byte publishCode) {
        this.publishCode = publishCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
