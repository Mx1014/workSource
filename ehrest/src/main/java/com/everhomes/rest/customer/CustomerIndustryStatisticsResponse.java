package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>customerTotalCount: 企业总数</li>
 *     <li>dtos: 统计信息 参考{@link com.everhomes.rest.customer.CustomerIndustryStatisticsDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerIndustryStatisticsResponse {

    private Long customerTotalCount;

    @ItemType(CustomerIndustryStatisticsDTO.class)
    private List<CustomerIndustryStatisticsDTO> dtos;

    public Long getCustomerTotalCount() {
        return customerTotalCount;
    }

    public void setCustomerTotalCount(Long customerTotalCount) {
        this.customerTotalCount = customerTotalCount;
    }

    public List<CustomerIndustryStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CustomerIndustryStatisticsDTO> dtos) {
        this.dtos = dtos;
    }
}
