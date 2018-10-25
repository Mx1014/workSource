package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 *     <li>widthRatio: widthRatio</li>
 *     <li>heightRatio: 宽高比（高）</li>
 *     <li>shadowFlag: shadowFlag</li>
 *     <li>paddingFlag: paddingFlag</li>
 * </ul>
 */
public class Banners {

    private String itemGroup;
    private Long widthRatio;
    private Long heightRatio;
    private Byte shadowFlag;
    private Integer paddingFlag;

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Long getWidthRatio() {
        return widthRatio;
    }

    public void setWidthRatio(Long widthRatio) {
        this.widthRatio = widthRatio;
    }

    public Long getHeightRatio() {
        return heightRatio;
    }

    public void setHeightRatio(Long heightRatio) {
        this.heightRatio = heightRatio;
    }

    public Byte getShadowFlag() {
        return shadowFlag;
    }

    public void setShadowFlag(Byte shadowFlag) {
        this.shadowFlag = shadowFlag;
    }

    public Integer getPaddingFlag() {
        return paddingFlag;
    }

    public void setPaddingFlag(Integer paddingFlag) {
        this.paddingFlag = paddingFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
