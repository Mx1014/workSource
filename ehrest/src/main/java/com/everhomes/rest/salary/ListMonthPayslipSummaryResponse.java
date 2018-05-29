// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>返回值:
 * <li>payslips: 工资条列表{@link com.everhomes.rest.salary.PayslipDTO}</li>
 * <li>nextPageAnchor: 下页锚点</li>
 * </ul>
 */
public class ListMonthPayslipSummaryResponse {

    @ItemType(PayslipDTO.class)
    private List<PayslipDTO> payslips;
    private Long nextPageAnchor;

    public ListMonthPayslipSummaryResponse() {

    }

    public ListMonthPayslipSummaryResponse(List<PayslipDTO> payslips, Long nextPageAnchor) {
        super();
        this.payslips = payslips;
        this.nextPageAnchor = nextPageAnchor;

    }

    public List<PayslipDTO> getPayslips() {
        return payslips;
    }

    public void setPayslips(List<PayslipDTO> payslips) {
        this.payslips = payslips;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
