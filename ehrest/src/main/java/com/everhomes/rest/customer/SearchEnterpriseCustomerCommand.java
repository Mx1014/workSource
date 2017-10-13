package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>keyword: 关键字：手机号或地址或客户名称</li>
 *     <li>customerCategoryId: 客户类型id</li>
 *     <li>levelId: 客户级别id</li>
 *     <li>communityId: 园区id</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 页面大小</li>
 *     <li>sortType: 排序类型：0 升序， 1 降序</li>
 *     <li>sortField: 排序字段名</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class SearchEnterpriseCustomerCommand {

    private String keyword;

    private Long customerCategoryId;

    private Long levelId;

    private Long communityId;

    private Long pageAnchor;

    private Integer pageSize;

    private Integer sortType;

    private String sortField;

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getCustomerCategoryId() {
        return customerCategoryId;
    }

    public void setCustomerCategoryId(Long customerCategoryId) {
        this.customerCategoryId = customerCategoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
