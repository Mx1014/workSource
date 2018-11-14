// @formatter:off
package com.everhomes.rest.launchpad;


/**
 * <ul>
 *     <li>LIST_VIEW("ListView"): 小图平铺（小图文列表）垂直列表 </li>
 *     <li>HORIZONTAL_SCROLL_VIEW("HorizontalScrollView"):小方形图文横向列表 </li>
 *     <li>LARGE_IMAGE_LIST_VIEW("LargeImageListView"): 大图平铺（大图文列表）垂直列表 </li>
 *     <li>HORIZONTAL_SCROLL_WIDE_VIEW("HorizontalScrollWideView"): 宽幅图文横向列表</li>
 *     <li>HORIZONTAL_SCROLL_SQUARE_VIEW("HorizontalScrollSquareView"): 方形图文横向列表</li>
 *     <li>TEXT_IMAGE_WITH_TAG_LIST_VIEW("TextImageWithTagListView"): 带标签图文垂直列表</li>
 *     <li>NEWS_LIST_VIEW("NewsListView"): 快讯图文列表样式</li>
 * </ul>
 */
public enum OPPushWidgetStyle {

    LIST_VIEW("ListView"), HORIZONTAL_SCROLL_VIEW("HorizontalScrollView"), LARGE_IMAGE_LIST_VIEW("LargeImageListView"), HORIZONTAL_SCROLL_WIDE_VIEW("HorizontalScrollWideView"),
    HORIZONTAL_SCROLL_SQUARE_VIEW("HorizontalScrollSquareView"), TEXT_IMAGE_WITH_TAG_LIST_VIEW("TextImageWithTagListView"), NEWS_LIST_VIEW("NewsListView");

    private String code;

    OPPushWidgetStyle(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static OPPushWidgetStyle fromCode(String code) {
        if (code != null) {
            for (OPPushWidgetStyle style : OPPushWidgetStyle.values()) {
                if (style.getCode().equals(code)) {
                    return style;
                }
            }
        }
        return null;
    }
}
