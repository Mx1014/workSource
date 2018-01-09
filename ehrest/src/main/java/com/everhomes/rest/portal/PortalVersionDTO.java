package com.everhomes.rest.portal;

import com.everhomes.rest.launchpad.PortalVersionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 版本id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>parentId: parentId</li>
 *     <li>syncVersion: syncVersion</li>
 *     <li>publishVersion: publishVersion</li>
 *     <li>syncTime: syncTime</li>
 *     <li>publishTime: publishTime</li>
 *     <li>status: 参考{@link PortalVersionStatus}</li>
 * </ul>
 */
public class PortalVersionDTO {

    private Long id;
    private Integer namespaceId;
    private Long parentId;
    private Integer syncVersion;
    private Integer publishVersion;
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

    public Integer getSyncVersion() {
        return syncVersion;
    }

    public void setSyncVersion(Integer syncVersion) {
        this.syncVersion = syncVersion;
    }

    public Integer getPublishVersion() {
        return publishVersion;
    }

    public void setPublishVersion(Integer publishVersion) {
        this.publishVersion = publishVersion;
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
}
