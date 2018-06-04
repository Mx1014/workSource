package com.everhomes.rest.enterpriseApproval;

/**
 * <ul>
 * <li>applierName: 申请人名称</li>
 * <li>applierDepartment: 申请人部门</li>
 * <li>applierJobPosition: 申请人职位</li>
 * <li>dismissTime: 离职日期</li>
 * <li>dismissReason: 离职原因</li>
 * <li>dismissRemark: 申请理由</li>
 * </ul>
 */
public class ComponentDismissApplicationValue {

    private String applierName;

    private String applierDepartment;

    private String applierJobPosition;

    private String dismissTime;

    private String dismissReason;

    private String dismissRemark;

    public ComponentDismissApplicationValue() {
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

    public String getDismissReason() {
        return dismissReason;
    }

    public void setDismissReason(String dismissReason) {
        this.dismissReason = dismissReason;
    }

    public String getDismissRemark() {
        return dismissRemark;
    }

    public void setDismissRemark(String dismissRemark) {
        this.dismissRemark = dismissRemark;
    }

    public String getDismissTime() {
        return dismissTime;
    }

    public void setDismissTime(String dismissTime) {
        this.dismissTime = dismissTime;
    }
}
