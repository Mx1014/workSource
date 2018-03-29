package com.everhomes.rest.socialSecurity;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>detailId: 用户detailId</li>
 * <li>userId: 用户id</li>
 * <li>contactName: 用户名称</li>
 * <li>departmentName: 部门名称</li>
 * <li>checkInTime: 入职时间</li>
 * <li>dismissTime: 离职时间</li>
 * <li>socialSecurityStatus: 社保状态 0-未缴 1-在缴 参考{@link SocialSecurityStatus}</li>
 * <li>accumulationFundStatus: 公积金状态 0-未缴 1-在缴 参考{@link SocialSecurityStatus}</li>
 * <li>socialSecurityStartMonth: 社保增月</li>
 * <li>socialSecurityEndMonth: 社保减月</li>
 * </ul>
 */
public class SocialSecurityEmployeeDTO {

    private Long detailId;

    private Long userId;

    private Integer namespaceId;

    private String contactName;

    private String departmentName;

    private Date checkInTime;

    private Date dismissTime;

    private Byte socialSecurityStatus;

    private Byte accumulationFundStatus;

    private String socialSecurityStartMonth;

    private String socialSecurityEndMonth;

    public SocialSecurityEmployeeDTO() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Byte getSocialSecurityStatus() {
        return socialSecurityStatus;
    }

    public void setSocialSecurityStatus(Byte socialSecurityStatus) {
        this.socialSecurityStatus = socialSecurityStatus;
    }

    public Byte getAccumulationFundStatus() {
        return accumulationFundStatus;
    }

    public void setAccumulationFundStatus(Byte accumulationFundStatus) {
        this.accumulationFundStatus = accumulationFundStatus;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
