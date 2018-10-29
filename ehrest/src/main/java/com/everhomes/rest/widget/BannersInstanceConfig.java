package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *      <li>categoryId: 应用入口ID</li>
 *      <li>moduleAppId: 应用ID</li>
 *      <Li>moreRouter: 查看更多路由  --非全屏轮播</Li>
 *      <li>backgroundType: 背景类型，0：无，1：纯色，2：渐变  --非全屏轮播</li>
 *      <li>color: 当背景类型为纯色时的颜色  --非全屏轮播 格式：#FFFFFF</li>
 *      <li>topColor: 当背景类型为渐变时的顶部颜色  --非全屏轮播 格式：#FFFFFF</li>
 *      <li>bottomColor: 当背景类型为渐变时的底部颜色  --非全屏轮播 格式：#FFFFFF</li>
 *      <li>autoScroll: 是否自动轮播，0为否，1为是。请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}  --非全屏轮播</li>
 *      <li>paddingFlag: 图片padding，是否有padding  0为否，1为是。请参考{@link com.everhomes.rest.common.TrueOrFalseFlag} --全屏轮播</li>
 *      <li>widthRatio: 宽比例数值  --全屏轮播</li>
 *      <li>heightRatio: 高比例数值  --全屏轮播</li>
 * </ul>
 */
public class BannersInstanceConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private String itemGroup;

	private Long categoryId;

	private Long moduleAppId;

	private String moreRouter;

	private Byte backgroundType;

	private String color;

	private String topColor;

	private String bottomColor;

	private Byte autoScroll;

    private Integer paddingFlag;

    private Long widthRatio;

    private Long heightRatio;

    public Integer getPaddingFlag() {
        return paddingFlag;
    }

    public void setPaddingFlag(Integer paddingFlag) {
        this.paddingFlag = paddingFlag;
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

    public Long getModuleAppId() {
        return moduleAppId;
    }

    public void setModuleAppId(Long moduleAppId) {
        this.moduleAppId = moduleAppId;
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

    public Byte getAutoScroll() {
        return autoScroll;
    }

    public void setAutoScroll(Byte autoScroll) {
        this.autoScroll = autoScroll;
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
