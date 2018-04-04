package com.everhomes.rest.portal;

import com.everhomes.rest.launchpad.PortalVersionStatus;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 版本id</li>
 *     <li>namespaceId: 与空间</li>
 *     <li>parentId: 父版本Id</li>
 *     <li>dateVersion: 日期本号</li>
 *     <li>bigVersion: 大版本号</li>
 *     <li>minorVersion: 小版本号</li>
 *     <li>createTime: 创建时间</li>
 *     <li>syncTime: 同步时间</li>
 *     <li>publishTime: 发布时间</li>
 *     <li>status: 参考{@link PortalVersionStatus}</li>
 * </ul>
 */
public class PortalVersionDTO {

    private Long id;
    private Integer namespaceId;
    private Long parentId;
    private Integer dateVersion;
    private Integer bigVersion;
    private Integer minorVersion;
    private Timestamp createTime;
    private Timestamp syncTime;
    private Timestamp publishTime;
    private Byte status;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getDateVersion() {
        return dateVersion;
    }

    public void setDateVersion(Integer dateVersion) {
        this.dateVersion = dateVersion;
    }

    public Integer getBigVersion() {
        return bigVersion;
    }

    public void setBigVersion(Integer bigVersion) {
        this.bigVersion = bigVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Timestamp syncTime) {
        this.syncTime = syncTime;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString(){return StringHelper.toJsonString(this);}
}
