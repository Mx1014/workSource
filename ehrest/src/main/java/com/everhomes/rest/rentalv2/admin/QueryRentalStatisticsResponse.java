package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalStatisticsDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>totalAmount: 总金额</li>
 * <li>orderCount: 总订单数量</li>
 * <li>classifyAmount: 分类统计结果</li>
 * </ul>
 */
public class QueryRentalStatisticsResponse {

    private BigDecimal totalAmount;
    private Integer orderCount;
    @ItemType(RentalStatisticsDTO.class)
    private List<RentalStatisticsDTO> classifyStatistics;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public List<RentalStatisticsDTO> getClassifyStatistics() {
        return classifyStatistics;
    }

    public void setClassifyStatistics(List<RentalStatisticsDTO> classifyStatistics) {
        this.classifyStatistics = classifyStatistics;
    }
}
