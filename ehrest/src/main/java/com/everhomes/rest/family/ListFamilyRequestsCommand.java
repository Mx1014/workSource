// @formatter:off
package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListFamilyRequestsCommand extends BaseCommand{
    
    private Long pageAnchor;;
    
    private Integer pageSize;
    
    public ListFamilyRequestsCommand() {
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
