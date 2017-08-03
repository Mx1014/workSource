package com.everhomes.rest.customer;

/**
 * <ul>
 *     <li>talentCategoryId:人才类型在系统中的id </li>
 *     <li>categoryName: 人才类型在域下的名称</li>
 *     <li>customerMemberCount: 人才数</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerTalentStatisticsDTO {

    private Long talentCategoryId;

    private String categoryName;

    private Long customerMemberCount;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getTalentCategoryId() {
        return talentCategoryId;
    }

    public void setTalentCategoryId(Long talentCategoryId) {
        this.talentCategoryId = talentCategoryId;
    }

    public Long getCustomerMemberCount() {
        return customerMemberCount;
    }

    public void setCustomerMemberCount(Long customerMemberCount) {
        this.customerMemberCount = customerMemberCount;
    }
}
