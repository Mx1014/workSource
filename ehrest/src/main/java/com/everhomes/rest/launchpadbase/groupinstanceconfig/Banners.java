package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 * </ul>
 */
public class Banners {

    private String itemGroup;
    private Long wideRatio;
    private Long hightRatio;
    private Byte shadowFlag;

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Long getWideRatio() {
        return wideRatio;
    }

    public void setWideRatio(Long wideRatio) {
        this.wideRatio = wideRatio;
    }

    public Long getHightRatio() {
        return hightRatio;
    }

    public void setHightRatio(Long hightRatio) {
        this.hightRatio = hightRatio;
    }

    public Byte getShadowFlag() {
        return shadowFlag;
    }

    public void setShadowFlag(Byte shadowFlag) {
        this.shadowFlag = shadowFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
