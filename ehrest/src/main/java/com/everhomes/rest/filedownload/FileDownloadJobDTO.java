// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>ownerId: ownerId</li>
 *     <li>fileName: fileName</li>
 *     <li>jobClassName: jobClassName</li>
 *     <li>jobParams: jobParams</li>
 *     <li>size: size</li>
 *     <li>uri: uri</li>
 *     <li>url: url</li>
 *     <li>rate: rate</li>
 *     <li>status: 状态 参考{@link JobStatus}</li>
 *     <li>createTime: createTime</li>
 *     <li>finishTime: finishTime</li>
 *     <li>updateTime: updateTime</li>
 * </ul>
 */
public class FileDownloadJobDTO {
    Long id;
    Long ownerId;
    String fileName;
    String jobClassName;
    String jobParams;
    Long size;
    String uri;
    String url;
    Float rate;
    Byte status;
    Timestamp createTime;
    Timestamp finishTime;
    Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getJobParams() {
        return jobParams;
    }

    public void setJobParams(String jobParams) {
        this.jobParams = jobParams;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
