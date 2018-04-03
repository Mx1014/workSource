package com.everhomes.rest.customer;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>projectSourceItemId: 项目来源在系统中的id </li>
 *     <li>itemName: 项目来源在域下的名称</li>
 *     <li>projectCount: 项目数</li>
 *     <li>projectAmount: 项目金额</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerProjectStatisticsDTO {

    private Long projectSourceItemId;

    private String itemName;

    private  Long projectCount;

    private BigDecimal projectAmount;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(BigDecimal projectAmount) {
        this.projectAmount = projectAmount;
    }

    public Long getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Long projectCount) {
        this.projectCount = projectCount;
    }

    public Long getProjectSourceItemId() {
        return projectSourceItemId;
    }

    public void setProjectSourceItemId(Long projectSourceItemId) {
        this.projectSourceItemId = projectSourceItemId;
    }
}
