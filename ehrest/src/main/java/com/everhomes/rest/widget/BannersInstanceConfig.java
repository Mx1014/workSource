package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *      <li>categoryId: 应用入口ID</li>
 *      <li>appId: 应用ID</li>
 *      <Li>moreRouter: 查看更多路由  --非全屏轮播</Li>
 *      <li>backgroundType: 背景类型，0：无，1：纯色，2：渐变  --非全屏轮播</li>
 *      <li>color: 当背景类型为纯色时的颜色  --非全屏轮播</li>
 *      <li>topColor: 当背景类型为渐变时的顶部颜色  --非全屏轮播</li>
 *      <li>bottomColor: 当背景类型为渐变时的底部颜色  --非全屏轮播</li>
 *      <li>autoCarousel: 是否自动轮播，0为否，1为是。请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}  --非全屏轮播</li>
 *      <li>padding: 图片padding  --全屏轮播</li>
 *      <li>scale: 图片比例，1. 5:3  2. 16:9   --全屏轮播</li>
 * </ul>
 */
public class BannersInstanceConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private String itemGroup;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

}
