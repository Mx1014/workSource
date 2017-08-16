package com.everhomes.rest.profile;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>contactName: 姓名</li>
 * <li>employeeStatus: 离职前状态</li>
 * <li>department: 部门</li>
 * <li>checkInTime: 入职日期</li>
 * <li>dimissTime: 离职日期</li>
 * <li>dismissType: 离职类型: 1-辞职,2-解雇,3-其他 参考{@link com.everhomes.rest.profile.DismissType}</li>
 * <li>dismissReason: 离职原因 参考{@link com.everhomes.rest.profile.DismissReason}</li>
 * <li>dimissRemarks: 备注</li>
 * <li>detailId: 成员detailId</li>
 * </ul>
 */
public class ProfileDismissEmployeeDTO {

    private Long organizationId;
    private String contactName;
    private Byte employeeStatus;
    private String department;
    private Date checkInTime;
    private Date dismissTime;
    private Byte dismissType;
    private String dismissReason;
    private String dismissRemarks;
    private Long detailId;

    public ProfileDismissEmployeeDTO() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public Byte getDismissType() {
        return dismissType;
    }

    public void setDismissType(Byte dismissType) {
        this.dismissType = dismissType;
    }

    public String getDismissReason() {
        return dismissReason;
    }

    public void setDismissReason(String dismissReason) {
        this.dismissReason = dismissReason;
    }

    public String getDismissRemarks() {
        return dismissRemarks;
    }

    public void setDismissRemarks(String dismissRemarks) {
        this.dismissRemarks = dismissRemarks;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
