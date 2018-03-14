package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>userId: 员工id</li>
 * <li>employeeNo: 员工编号(可为空)</li>
 * <li>contactName: 员工姓名</li>
 * <li>dismissTime: 离职日期 </li>
 * <li>checkInTime: 入职日期 </li>
 * <li>regularSalary: 固定工资合计</li>
 * <li>shouldPaySalary: 应发合计</li>
 * <li>realPaySalary: 实发合计</li>
 * <li>salaryStatus: 状态: 0 正常 1 实发合计为负 2 未定薪</li>
 * </ul>
 */
public class SalaryEmployeeDTO {

    private Long id;
    private String contactName;
    private Long ownerId;
    private Long dismissTime;
    private Long checkInTime;

    private Long userDetailId;
    private BigDecimal regularSalary;
    private BigDecimal shouldPaySalary;
    private BigDecimal realPaySalary;
    private Byte salaryStatus;

    public SalaryEmployeeDTO() {
    }

    public Long getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Long checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getDismissTime() {
        return dismissTime;
    }

    public void setDismissTime(Long dismissTime) {
        this.dismissTime = dismissTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public BigDecimal getRealPaySalary() {
        return realPaySalary;
    }

    public void setRealPaySalary(BigDecimal realPaySalary) {
        this.realPaySalary = realPaySalary;
    }

    public BigDecimal getRegularSalary() {
        return regularSalary;
    }

    public void setRegularSalary(BigDecimal regularSalary) {
        this.regularSalary = regularSalary;
    }

    public Byte getSalaryStatus() {
        return salaryStatus;
    }

    public void setSalaryStatus(Byte salaryStatus) {
        this.salaryStatus = salaryStatus;
    }

    public BigDecimal getShouldPaySalary() {
        return shouldPaySalary;
    }

    public void setShouldPaySalary(BigDecimal shouldPaySalary) {
        this.shouldPaySalary = shouldPaySalary;
    }

    public Long getUserDetailId() {
        return userDetailId;
    }

    public void setUserDetailId(Long userDetailId) {
        this.userDetailId = userDetailId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
