package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset：下一页</li>
 * <li>employeePaymentLogDTOS: 支付记录 {@link com.everhomes.rest.enterprisepaymentauth.EmployeePaymentLogDTO}</li>
 * </ul>
 */
public class ListEmployeePaymentLogsResponse {
    private Integer nextPageOffset;
    private List<EmployeePaymentLogDTO> employeePaymentLogDTOS;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<EmployeePaymentLogDTO> getEmployeePaymentLogDTOS() {
        return employeePaymentLogDTOS;
    }

    public void setEmployeePaymentLogDTOS(List<EmployeePaymentLogDTO> employeePaymentLogDTOS) {
        this.employeePaymentLogDTOS = employeePaymentLogDTOS;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
