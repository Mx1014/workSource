package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>customerTotalCount: 企业总数</li>
 *     <li>dtos: 统计信息 参考{@link com.everhomes.rest.customer.CustomerSourceStatisticsDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerSourceStatisticsResponse {

    private Long customerTotalCount;

    @ItemType(CustomerSourceStatisticsDTO.class)
    private List<CustomerSourceStatisticsDTO> dtos;

    public Long getCustomerTotalCount() {
        return customerTotalCount;
    }

    public void setCustomerTotalCount(Long customerTotalCount) {
        this.customerTotalCount = customerTotalCount;
    }

    public List<CustomerSourceStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CustomerSourceStatisticsDTO> dtos) {
        this.dtos = dtos;
    }
}
