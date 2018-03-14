package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 * <li>payMonth: 月份</li>
 * <li>creatorUid: 创建人</li>
 * <li>createTime: 创建时间</li>
 * <li>fileUid: 归档人</li>
 * <li>fileTime: 归档时间</li>
 * </ul>
 */
public class GetSocialSecurityReportsHeadResponse {

    private String payMonth;
    private Long creatorUid;
    private String creatorName;
    private Long createTime;
    private Long fileUid;
    private Long fileTime;
    private String fileName;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Long getFileTime() {
        return fileTime;
    }

    public void setFileTime(Long fileTime) {
        this.fileTime = fileTime;
    }

    public Long getFileUid() {
        return fileUid;
    }

    public void setFileUid(Long fileUid) {
        this.fileUid = fileUid;
    }


    public String getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(String payMonth) {
        this.payMonth = payMonth;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
