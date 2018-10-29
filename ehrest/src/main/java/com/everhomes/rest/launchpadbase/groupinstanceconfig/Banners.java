package com.everhomes.rest.launchpadbase.groupinstanceconfig;

import com.everhomes.rest.launchpadbase.ItemGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>itemGroup: itemGroup</li>
 *      <li>categoryId: 应用入口ID</li>
 *      <li>appId: 应用ID</li>
 *      <Li>moreRouter: 查看更多路由  --非全屏轮播</Li>
 *      <li>backgroundType: 背景类型，0：无，1：纯色，2：渐变  --非全屏轮播</li>
 *      <li>color: 当背景类型为纯色时的颜色  --非全屏轮播 格式：#FFFFFF</li>
 *      <li>topColor: 当背景类型为渐变时的顶部颜色  --非全屏轮播 格式：#FFFFFF</li>
 *      <li>bottomColor: 当背景类型为渐变时的底部颜色  --非全屏轮播 格式：#FFFFFF</li>
 *      <li>autoScroll: 是否自动轮播，0为否，1为是。请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}  --非全屏轮播</li>
 *      <li>paddingFlag: 图片padding，是否有padding  --全屏轮播</li>
 *      <li>widthRatio: 宽比例数值  --全屏轮播</li>
 *      <li>heightRatio: 高比例数值  --全屏轮播</li>
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

    private Byte autoScroll;

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
