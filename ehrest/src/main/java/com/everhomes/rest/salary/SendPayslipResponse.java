package com.everhomes.rest.salary;

/**
 *
 * <ul>
 * <li>payslipId: 工资条id</li>
 * </ul>
 */
public class SendPayslipResponse {
    private Long payslipId;

    public SendPayslipResponse(){}

    public SendPayslipResponse(Long payslipId) {
        super();
        this.payslipId = payslipId;
    }
    public Long getPayslipId() {
        return payslipId;
    }

    public void setPayslipId(Long payslipId) {
        this.payslipId = payslipId;
    }
}
