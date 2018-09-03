package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

import java.sql.Date;

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

    private Long    id ;
    private Integer   namespaceId ;String   infoType ;
    private String   commitVersion ;
    private Date createTime ;
    private Date   publishTime ;
    private Byte   publishCode ;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
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
