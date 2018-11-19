// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>组内控件类型
 * <li>DEFAULT("Default")：默认</li>
 *
 * <li>NAVIGATOR_METRO("Metro"): Metro win8风格</li>
 * <li>NAVIGATOR_LIGHT("Light")：Light 轻风格</li>
 * <li>NAVIGATOR_GLLERY("Gallery")：Gallery 自定义风格</li>
 *
 * <li>OPPUSH_LIST_VIEW("ListView")：列表</li>
 * <li>OPPUSH_HORIZONTAL_SCROLL_VIEW("HorizontalScrollView")：左右滑动</li>
 * <li>OPPUSH_LARGE_IMAGE_LIST_VIEW("LargeImageListView")：大图列表</li>
 *
 * <li>PURE_TEXT("PureText")：纯文本</li>
 * <li>TEXT_WITH_BOARD("TextWithBoard")：带边框的文本</li>
 * <li>TEXT_WITH_ICON("TextWithIcon")：icon型（小图）</li>
 * <li>TEXT_WITH_IMAGE("TextWithImage")：image型（大图）</li>
 * <li>FOLD("Fold"): 折叠样式</li>
 * </ul>
 */
public enum PortalItemGroupWidgetStyle {

    DEFAULT("Default"),
    NAVIGATOR_METRO("Metro"),
    NAVIGATOR_LIGHT("Light"),
    NAVIGATOR_GLLERY("Gallery"),
    OPPUSH_LIST_VIEW("ListView"),
    OPPUSH_HORIZONTAL_SCROLL_VIEW("HorizontalScrollView"),
    OPPUSH_LARGE_IMAGE_LIST_VIEW("LargeImageListView"),
    PURE_TEXT("PureText"),
    TEXT_WITH_BOARD("TextWithBoard"),
    TEXT_WITH_ICON("TextWithIcon"),
    TEXT_WITH_IMAGE("TextWithImage"),
    FOLD("Fold");

    private String code;

    private PortalItemGroupWidgetStyle(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PortalItemGroupWidgetStyle fromCode(String code) {
        if(null != code){
            for (PortalItemGroupWidgetStyle value: PortalItemGroupWidgetStyle.values()) {
                if(value.code.equals(code)){
                    return value;
                }
            }
        }
        return null;
    }
}
