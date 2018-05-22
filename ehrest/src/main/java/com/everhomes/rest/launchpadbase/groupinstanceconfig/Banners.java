package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 *     <li>wideRatio: 宽高比（宽）</li>
 *     <li>hightRatio: 宽高比（高）</li>
 *     <li>shadowFlag: shadowFlag</li>
 *     <li>paddingTop: paddingTop</li>
 *     <li>paddingLeft: paddingLeft</li>
 *     <li>paddingBottom: paddingBottom</li>
 *     <li>paddingRight: paddingRight</li>
 * </ul>
 */
public class Banners {

    private String itemGroup;
    private Long wideRatio;
    private Long hightRatio;
    private Byte shadowFlag;
    private Integer paddingTop;
    private Integer paddingLeft;
    private Integer paddingBottom;
    private Integer paddingRight;

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

    public Integer getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(Integer paddingTop) {
        this.paddingTop = paddingTop;
    }

    public Integer getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(Integer paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public Integer getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(Integer paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public Integer getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(Integer paddingRight) {
        this.paddingRight = paddingRight;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
