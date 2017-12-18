// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>orgId: orgId</li>
 *     <li>userId: userId</li>
 *     <li>ownerId: ownerId</li>
 *     <li>name: name</li>
 *     <li>fileName: fileName</li>
 *     <li>type: type</li>
 *     <li>params: params</li>
 *     <li>repeatFlag: repeatFlag</li>
 *     <li>process: process</li>
 *     <li>size: size</li>
 *     <li>uri: uri</li>
 *     <li>url: url</li>
 *     <li>rate: rate</li>
 *     <li>status: 状态 参考{@link TaskStatus}</li>
 *     <li>errorDescription: errorDescription</li>
 *     <li>createTime: createTime</li>
 *     <li>finishTime: finishTime</li>
 *     <li>updateTime: updateTime</li>
 * </ul>
 */
public class FileDownloadTaskDTO {
    Long id;
    Integer namespaceId;
    Long communityId;
    Long orgId;
    Long userId;
    Long ownerId;
    String name;
    String fileName;
    Byte type;
    String params;
    Byte repeatFlag;
    Integer process;
    Long size;
    String uri;
    String url;
    Float rate;
    Byte status;
    String errorDescription;
    Timestamp createTime;
    Timestamp finishTime;
    Timestamp updateTime;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Byte getRepeatFlag() {
        return repeatFlag;
    }

    public void setRepeatFlag(Byte repeatFlag) {
        this.repeatFlag = repeatFlag;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
