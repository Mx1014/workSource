package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>sourceItemId: 企业认知途径在系统中的id </li>
 *     <li>itemName: 企业认知途径在域下的名称</li>
 *     <li>customerCount: 企业数</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerSourceStatisticsDTO {

    private Long sourceItemId;

    private String itemName;

    private Long customerCount;

    public Long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getSourceItemId() {
        return sourceItemId;
    }

    public void setSourceItemId(Long sourceItemId) {
        this.sourceItemId = sourceItemId;
    }
}
