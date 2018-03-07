package com.everhomes.rest.banner.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>scope: 小区id</li>
 *     <li>count: 开启数量</li>
 * </ul>
 */
public class EnabledBannersDTO {

    private Long scope;
    private Integer count;

    public EnabledBannersDTO() {
    }

    public EnabledBannersDTO(Long scope, Integer count) {
        this.scope = scope;
        this.count = count;
    }

    public Long getScope() {
        return scope;
    }

    public void setScope(Long scope) {
        this.scope = scope;
    }

    public Integer getCount() {

        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
