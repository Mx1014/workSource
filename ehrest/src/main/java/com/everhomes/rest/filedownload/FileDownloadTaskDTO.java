// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>orgId: 公司id</li>
 *     <li>userId: 用户id</li>
 *     <li>name: 任务名称</li>
 *     <li>fileName: 文件名称</li>
 *     <li>type: 任务类型</li>
 *     <li>params: 任务参数</li>
 *     <li>repeatFlag: 是否可重复执行</li>
 *     <li>process: 任务进度</li>
 *     <li>size: 文件大小</li>
 *     <li>uri: 文件uri</li>
 *     <li>url: 文件url</li>
 *     <li>status: 状态 参考{@link TaskStatus}</li>
 *     <li>queueCount: 前面排队总数</li>
 *     <li>errorDescription: 错误描述</li>
 *     <li>createTime: 创建时间</li>
 *     <li>validTime: 有效期</li>
 *     <li>finishTime: 完成时间</li>
 *     <li>updateTime: 最后更新时间</li>
 *     <li>executeStartTime: 任务开始执行时间</li>
 *     <li>uploadFileStartTime: 开始上传文件时间</li>
 *     <li>uploadFileFinishTime: 上传文件结束时间</li>
 * </ul>
 */
public class FileDownloadTaskDTO {
    Long id;
    Integer namespaceId;
    Long communityId;
    Long orgId;
    Long userId;
    String name;
    String fileName;
    Byte type;
    String params;
    Byte repeatFlag;
    Integer process;
    Long size;
    String uri;
    String url;
    Byte status;
    Integer queueCount;
    String errorDescription;
    Timestamp createTime;
    Timestamp validTime;
    Timestamp finishTime;
    Timestamp updateTime;
    Timestamp executeStartTime;
    Timestamp uploadFileStartTime;
    Timestamp uploadFileFinishTime;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getQueueCount() {
        return queueCount;
    }

    public void setQueueCount(Integer queueCount) {
        this.queueCount = queueCount;
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

    public Timestamp getValidTime() {
        return validTime;
    }

    public void setValidTime(Timestamp validTime) {
        this.validTime = validTime;
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

    public Timestamp getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(Timestamp executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    public Timestamp getUploadFileStartTime() {
        return uploadFileStartTime;
    }

    public void setUploadFileStartTime(Timestamp uploadFileStartTime) {
        this.uploadFileStartTime = uploadFileStartTime;
    }

    public Timestamp getUploadFileFinishTime() {
        return uploadFileFinishTime;
    }

    public void setUploadFileFinishTime(Timestamp uploadFileFinishTime) {
        this.uploadFileFinishTime = uploadFileFinishTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
