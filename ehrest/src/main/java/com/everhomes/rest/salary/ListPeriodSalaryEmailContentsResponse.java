package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *
 * <ul>返回值:
 * <li>salaryGroupResps: 字段项列表 参考{@link com.everhomes.rest.salary.GetPeriodSalaryEmailContentResponse}</li>
 * </ul>
 */
public class ListPeriodSalaryEmailContentsResponse {
    @ItemType(GetPeriodSalaryEmailContentResponse.class)
    private List<GetPeriodSalaryEmailContentResponse> salaryGroupResps;

    public List<GetPeriodSalaryEmailContentResponse> getSalaryGroupResps() {
        return salaryGroupResps;
    }

    public void setSalaryGroupResps(List<GetPeriodSalaryEmailContentResponse> salaryGroupResps) {
        this.salaryGroupResps = salaryGroupResps;
    }
}
