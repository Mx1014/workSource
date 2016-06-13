package com.everhomes.rest.aclink;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class CreateAclinkFirmwareCommand {
    private String     infoUrl;
    private Long     checksum;
    private String     md5sum;
    
    @NotNull
    private String     downloadUrl;
    
    @NotNull
    private Byte     major;
    
    @NotNull
    private Byte     minor;
    
    
    private Byte     revision;
    
    public String getInfoUrl() {
        return infoUrl;
    }
    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
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
    public String getDownloadUrl() {
        return downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public Byte getMajor() {
        return major;
    }
    public void setMajor(Byte major) {
        this.major = major;
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
