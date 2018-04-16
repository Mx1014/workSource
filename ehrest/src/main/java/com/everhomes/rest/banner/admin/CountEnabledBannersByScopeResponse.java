package com.everhomes.rest.banner.admin;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>list: 小区和开启banner数量对象 {@link com.everhomes.rest.banner.admin.EnabledBannersDTO}</li>
 * </ul>
 */
public class CountEnabledBannersByScopeResponse {

    private List<EnabledBannersDTO> list;

    public List<EnabledBannersDTO> getList() {
        return list;
    }

    public void setList(List<EnabledBannersDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
