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

    private String paymentMonth;
    private Long creatorUid;
    private Long createTime;
    private Long fileUid;
    private Long fileTime;


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

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }
}
