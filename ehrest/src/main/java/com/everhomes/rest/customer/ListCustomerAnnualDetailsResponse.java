package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>statistics: 月份营业额纳税额统计信息 参考{@link com.everhomes.rest.customer.MonthStatistics}</li>
 *     <li>quarterStatisticses: 季度营业额纳税额统计信息 参考{@link com.everhomes.rest.customer.QuarterStatistics}</li>
 * </ul>
 * Created by ying.xiong on 2017/11/8.
 */
public class ListCustomerAnnualDetailsResponse {
    @ItemType(MonthStatistics.class)
    private List<MonthStatistics> statistics;

    @ItemType(QuarterStatistics.class)
    private List<QuarterStatistics> quarterStatisticses;

    public List<QuarterStatistics> getQuarterStatisticses() {
        return quarterStatisticses;
    }

    public void setQuarterStatisticses(List<QuarterStatistics> quarterStatisticses) {
        this.quarterStatisticses = quarterStatisticses;
    }

    public List<MonthStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<MonthStatistics> statistics) {
        this.statistics = statistics;
    }
}
