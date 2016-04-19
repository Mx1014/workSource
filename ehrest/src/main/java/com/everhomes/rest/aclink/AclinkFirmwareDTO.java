package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class AclinkFirmwareDTO {
    private String     infoUrl;
    private Byte     status;
    private Byte     major;
    private Byte     firmwareType;
    private Long     checksum;
    private String     md5sum;
    private Byte     ownerType;
    private Timestamp     createTime;
    private String     downloadUrl;
    private Long     creatorId;
    private Long     ownerId;
    private Long     id;
    private Byte     minor;
    private Byte     revision;

    

    public String getInfoUrl() {
        return infoUrl;
    }



    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }



    public Byte getStatus() {
        return status;
    }



    public void setStatus(Byte status) {
        this.status = status;
    }



    public Byte getMajor() {
        return major;
    }



    public void setMajor(Byte major) {
        this.major = major;
    }



    public Byte getFirmwareType() {
        return firmwareType;
    }



    public void setFirmwareType(Byte firmwareType) {
        this.firmwareType = firmwareType;
    }



    public Long getChecksum() {
        return checksum;
    }



    public void setChecksum(Long checksum) {
        this.checksum = checksum;
    }



    public String getMd5sum() {
        return md5sum;
    }



    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }



    public Byte getOwnerType() {
        return ownerType;
    }



    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }



    public Timestamp getCreateTime() {
        return createTime;
    }



    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    public String getDownloadUrl() {
        return downloadUrl;
    }



    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }



    public Long getCreatorId() {
        return creatorId;
    }



    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }



    public Long getOwnerId() {
        return ownerId;
    }



    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public Byte getMinor() {
        return minor;
    }



    public void setMinor(Byte minor) {
        this.minor = minor;
    }



    public Byte getRevision() {
        return revision;
    }



    public void setRevision(Byte revision) {
        this.revision = revision;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}