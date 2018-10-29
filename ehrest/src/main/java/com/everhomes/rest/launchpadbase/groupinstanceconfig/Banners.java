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
 *      <li>categoryId: 应用入口ID</li>
 *      <li>appId: 应用ID</li>
 *      <Li>moreRouter: 查看更多路由</Li>
 *      <li>backgroundType: 背景类型，0：无，1：纯色，2：渐变</li>
 *      <li>color: 当背景类型为纯色时的颜色</li>
 *      <li>topColor: 当背景类型为渐变时的顶部颜色</li>
 *      <li>bottomColor: 当背景类型为渐变时的底部颜色</li>
 *      <li>autoCarousel: 是否自动轮播，0为否，1为是。请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *      <li>padding: 图片padding</li>
 *      <li>scale: 图片比例，1. 5:3  2. 16:9</li>
 * </ul>
 */
public class Banners {

    private String itemGroup;
    private Long widthRatio;
    private Long heightRatio;
    private Byte shadowFlag;
    private Integer paddingFlag;

    private Long categoryId;

    private Long appId;

    private String moreRouter;

    private Byte backgroundType;

    private String color;

    private String topColor;

    private String bottomColor;

    private Byte autoCarousel;

    private Long padding;

    private Byte scale;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getMoreRouter() {
        return moreRouter;
    }

    public void setMoreRouter(String moreRouter) {
        this.moreRouter = moreRouter;
    }

    public Byte getBackgroundType() {
        return backgroundType;
    }

    public void setBackgroundType(Byte backgroundType) {
        this.backgroundType = backgroundType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTopColor() {
        return topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public String getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(String bottomColor) {
        this.bottomColor = bottomColor;
    }

    public Byte getAutoCarousel() {
        return autoCarousel;
    }

    public void setAutoCarousel(Byte autoCarousel) {
        this.autoCarousel = autoCarousel;
    }

    public Long getPadding() {
        return padding;
    }

    public void setPadding(Long padding) {
        this.padding = padding;
    }

    public Byte getScale() {
        return scale;
    }

    public void setScale(Byte scale) {
        this.scale = scale;
    }

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
