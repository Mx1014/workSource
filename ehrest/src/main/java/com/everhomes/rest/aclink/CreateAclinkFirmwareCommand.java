package com.everhomes.rest.aclink;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>infoUrl: 固件说明页面</li>
 * <li>checksum: 固件校验码</li>
 * <li>md5sum: 固件md5值</li>
 * <li>firmwareType: 固件类型，默认 zuolin </li>
 * <li>downloadUrl: 下载url </li>
 * <li>major: 主版本号 </li>
 * <li>minor: 次版本好 </li>
 * <li>revision: 修订版本号 </li>
 * </ul>
 * @author janson
 *
 */
public class CreateAclinkFirmwareCommand {
    private String     infoUrl;
    private Long     checksum;
    private String     md5sum;
    private String firmwareType;
    
    @NotNull
    private String     downloadUrl;
    
    @NotNull
    private Integer     major;
    
    @NotNull
    private Integer     minor;
    
    
    private Integer     revision;
    
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

    public Integer getMajor() {
        return major;
    }
    public void setMajor(Integer major) {
        this.major = major;
    }
    public Integer getMinor() {
        return minor;
    }
    public void setMinor(Integer minor) {
        this.minor = minor;
    }
    public Integer getRevision() {
        return revision;
    }
    public void setRevision(Integer revision) {
        this.revision = revision;
    }
    public String getFirmwareType() {
        return firmwareType;
    }
    public void setFirmwareType(String firmwareType) {
        this.firmwareType = firmwareType;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
