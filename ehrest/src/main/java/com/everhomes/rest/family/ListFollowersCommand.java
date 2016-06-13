// @formatter:off
package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>pageOffset: 页码（可选）</li>
 * </ul>
 */
public class ListFollowersCommand extends BaseCommand{

    private Long pageOffset;

    public ListFollowersCommand() {
    }
    
    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
