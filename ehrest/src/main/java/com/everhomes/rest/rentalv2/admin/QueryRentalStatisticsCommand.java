package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalStatisticsDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>totalAmount: 总金额</li>
 * <li>orderAmount: 总订单数量</li>
 * <li>classifyAmount: 分类统计结果</li>
 * </ul>
 */
public class QueryRentalStatisticsCommand {

    private BigDecimal totalAmount;
    private Integer orderAmount;
    @ItemType(RentalStatisticsDTO.class)
    private List<RentalStatisticsDTO> classifyAmount;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public List<RentalStatisticsDTO> getClassifyAmount() {
        return classifyAmount;
    }

    public void setClassifyAmount(List<RentalStatisticsDTO> classifyAmount) {
        this.classifyAmount = classifyAmount;
    }
}
