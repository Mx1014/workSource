package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>projectTotalCount: 项目总数</li>
 *     <li>projectTotalAmount: 项目总金额数</li>
 *     <li>dtos: 统计信息 参考{@link com.everhomes.rest.customer.CustomerProjectStatisticsDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerProjectStatisticsResponse {

    private Long projectTotalCount;

    private BigDecimal projectTotalAmount;

    @ItemType(CustomerProjectStatisticsDTO.class)
    private List<CustomerProjectStatisticsDTO> dtos;

    public List<CustomerProjectStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CustomerProjectStatisticsDTO> dtos) {
        this.dtos = dtos;
    }

    public BigDecimal getProjectTotalAmount() {
        return projectTotalAmount;
    }

    public void setProjectTotalAmount(BigDecimal projectTotalAmount) {
        this.projectTotalAmount = projectTotalAmount;
    }

    public Long getProjectTotalCount() {
        return projectTotalCount;
    }

    public void setProjectTotalCount(Long projectTotalCount) {
        this.projectTotalCount = projectTotalCount;
    }
}
