package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *      <li>statisticDTOs: 企业年度营业额纳税额，参考{@link com.everhomes.rest.customer.CustomerAnnualStatisticDTO}</li>
 *      <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 * Created by ying.xiong on 2017/11/8.
 */
public class ListCustomerAnnualStatisticsResponse {
    @ItemType(CustomerAnnualStatisticDTO.class)
    private List<CustomerAnnualStatisticDTO> statisticDTOs;

    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<CustomerAnnualStatisticDTO> getStatisticDTOs() {
        return statisticDTOs;
    }

    public void setStatisticDTOs(List<CustomerAnnualStatisticDTO> statisticDTOs) {
        this.statisticDTOs = statisticDTOs;
    }
}
