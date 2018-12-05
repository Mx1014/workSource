// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>employeeMonthLimitAmount: 人员每月限额 </li>
 * <li>employees: 选择的员工列表，参考{@link com.everhomes.rest.enterprisepaymentauth.EmployeeSimpleDTO}</li>
 * <li>employeePaymentSceneLimits: 场景限额{@link com.everhomes.rest.enterprisepaymentauth.EmployeePaymentSceneLimitSimpleDTO}</li>
 * </ul>
 */
public class BatchCreateOrUpdateEmployeePaymentLimitCommand {
    private Long organizationId;
    private BigDecimal employeeMonthLimitAmount;
    private List<EmployeeSimpleDTO> employees;
    private List<EmployeePaymentSceneLimitSimpleDTO> employeePaymentSceneLimits;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public BigDecimal getEmployeeMonthLimitAmount() {
        return employeeMonthLimitAmount;
    }

    public void setEmployeeMonthLimitAmount(BigDecimal employeeMonthLimitAmount) {
        this.employeeMonthLimitAmount = employeeMonthLimitAmount;
    }

    public List<EmployeeSimpleDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeSimpleDTO> employees) {
        this.employees = employees;
    }

    public List<EmployeePaymentSceneLimitSimpleDTO> getEmployeePaymentSceneLimits() {
        return employeePaymentSceneLimits;
    }

    public void setEmployeePaymentSceneLimits(List<EmployeePaymentSceneLimitSimpleDTO> employeePaymentSceneLimits) {
        this.employeePaymentSceneLimits = employeePaymentSceneLimits;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
