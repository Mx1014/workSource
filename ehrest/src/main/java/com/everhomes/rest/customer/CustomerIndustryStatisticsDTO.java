package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>corpIndustryItemId:行业在系统中的id </li>
 *     <li>itemName: 行业在域下的名称</li>
 *     <li>customerCount: 企业数</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerIndustryStatisticsDTO {

    private Long corpIndustryItemId;

    private String itemName;

    private Long customerCount;

    public Long getCorpIndustryItemId() {
        return corpIndustryItemId;
    }

    public void setCorpIndustryItemId(Long corpIndustryItemId) {
        this.corpIndustryItemId = corpIndustryItemId;
    }

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
}
