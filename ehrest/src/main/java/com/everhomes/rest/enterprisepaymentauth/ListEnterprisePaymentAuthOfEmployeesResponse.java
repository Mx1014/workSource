// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset: 下一个页码</li>
 * <li>employeeAuths: 员工限额列表 参考{@link com.everhomes.rest.enterprisepaymentauth.EmployeePaymentAuthDTO}</li>
 * </ul>
 */
public class ListEnterprisePaymentAuthOfEmployeesResponse {
    private Integer nextPageOffset;
    private List<EmployeePaymentAuthDTO> employeeAuths;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<EmployeePaymentAuthDTO> getEmployeeAuths() {
        return employeeAuths;
    }

    public void setEmployeeAuths(List<EmployeePaymentAuthDTO> employeeAuths) {
        this.employeeAuths = employeeAuths;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
