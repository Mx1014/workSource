package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: 门户板块，例如：活动为OPPushActivity。以前是使用itemGroup区分那种类型的，之后使用模块，moduleId由appId查询得到</li>
 *     <li>appId: appId</li>
 *     <li>newsSize</li>
 * </ul>
 */
public class OPPush {

    private String itemGroup;

    private Long appId;

    private Integer newsSize;

    public Integer getNewsSize() {
        return newsSize;
    }

    public void setNewsSize(Integer newsSize) {
        this.newsSize = newsSize;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
