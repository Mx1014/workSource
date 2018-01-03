package com.everhomes.rest.socialSecurity;

import java.sql.Date;

/**
 * <ul>
 * <li>detailId: 用户detailId</li>
 * <li>userId: 用户id</li>
 * <li>checkInTime: 入职时间</li>
 * <li>dismissTime: 离职时间</li>
 * <li>socialSecurityStartMonth: socialSecurityStartMonth</li>
 * <li>socialSecurityEndMonth: socialSecurityEndMonth</li>
 * </ul>
 */
public class SocialSecurityEmployeeDTO {

    private Long detailId;

    private Long userId;

    private Date checkInTime;

    private Date dismissTime;

    private String socialSecurityStartMonth;

    private String socialSecurityEndMonth;

    public SocialSecurityEmployeeDTO() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getDismissTime() {
        return dismissTime;
    }

    public void setDismissTime(Date dismissTime) {
        this.dismissTime = dismissTime;
    }

    public String getSocialSecurityStartMonth() {
        return socialSecurityStartMonth;
    }

    public void setSocialSecurityStartMonth(String socialSecurityStartMonth) {
        this.socialSecurityStartMonth = socialSecurityStartMonth;
    }

    public String getSocialSecurityEndMonth() {
        return socialSecurityEndMonth;
    }

    public void setSocialSecurityEndMonth(String socialSecurityEndMonth) {
        this.socialSecurityEndMonth = socialSecurityEndMonth;
    }
}
