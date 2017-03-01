// @formatter:off
package com.everhomes.rest.launchpad;


/**
 * <ul>
 *     <li>LIST_VIEW("ListView"): 列表</li>
 *     <li>HORIZONTAL_SCROLL_VIEW("HorizontalScrollView"): 左右滑动</li>
 *     <li>LARGE_IMAGE_LIST_VIEW("LargeImageListView"): 大图列表</li>
 * </ul>
 */
public enum OPPushWidgetStyle {

    LIST_VIEW("ListView"), HORIZONTAL_SCROLL_VIEW("HorizontalScrollView"), LARGE_IMAGE_LIST_VIEW("LargeImageListView");

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
