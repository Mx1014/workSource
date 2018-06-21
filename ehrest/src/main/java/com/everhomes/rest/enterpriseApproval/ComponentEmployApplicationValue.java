package com.everhomes.rest.enterpriseApproval;

/**
 * <ul>
 * <li>applierName: 申请人名称</li>
 * <li>applierDepartment: 申请人部门</li>
 * <li>applierJobPosition: 申请人职位</li>
 * <li>checkInTime: 入职时间</li>
 * <li>employmentTime: 转正时间</li>
 * <li>employmentReason: 转正理由</li>
 * </ul>
 */
public class ComponentEmployApplicationValue {

    private String applierName;

    private String applierDepartment;

    private String applierJobPosition;

    private String checkInTime;

    private String employmentTime;

    private String employmentReason;

    public ComponentEmployApplicationValue() {
    }

    public String getApplierName() {
        return applierName;
    }

    public void setApplierName(String applierName) {
        this.applierName = applierName;
    }

    public String getApplierDepartment() {
        return applierDepartment;
    }

    public void setApplierDepartment(String applierDepartment) {
        this.applierDepartment = applierDepartment;
    }

    public String getApplierJobPosition() {
        return applierJobPosition;
    }

    public void setApplierJobPosition(String applierJobPosition) {
        this.applierJobPosition = applierJobPosition;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(String employmentTime) {
        this.employmentTime = employmentTime;
    }

    public String getEmploymentReason() {
        return employmentReason;
    }

    public void setEmploymentReason(String employmentReason) {
        this.employmentReason = employmentReason;
    }
}
