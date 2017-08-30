package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>propertyTotalCount: 知识产权总数</li>
 *     <li>dtos: 统计信息 参考{@link com.everhomes.rest.customer.CustomerIntellectualPropertyStatisticsDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerIntellectualPropertyStatisticsResponse {
    private Long propertyTotalCount;

    @ItemType(CustomerIntellectualPropertyStatisticsDTO.class)
    private List<CustomerIntellectualPropertyStatisticsDTO> dtos;

    public Long getPropertyTotalCount() {
        return propertyTotalCount;
    }

    public void setPropertyTotalCount(Long propertyTotalCount) {
        this.propertyTotalCount = propertyTotalCount;
    }

    public List<CustomerIntellectualPropertyStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CustomerIntellectualPropertyStatisticsDTO> dtos) {
        this.dtos = dtos;
    }
}
