package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>statistics: 月份营业额纳税额统计信息 参考{@link com.everhomes.rest.customer.MonthStatistics}</li>
 * </ul>
 * Created by ying.xiong on 2017/11/8.
 */
public class ListCustomerAnnualDetailsResponse {
    @ItemType(MonthStatistics.class)
    private List<MonthStatistics> statistics;

    public List<MonthStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<MonthStatistics> statistics) {
        this.statistics = statistics;
    }
}
