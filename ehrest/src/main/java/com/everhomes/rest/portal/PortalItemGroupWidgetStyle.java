// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>组内控件类型
 * <li>NAVIGATOR：导航</li>
 * <li>BANNERS: banner</li>
 * <li>BULLETINS：滚动广告组件</li>
 * <li>NEWS：有时间轴新闻版块</li>
 * <li>NEWS_FLASH：无时间轴新闻版块</li>
 * <li>OPPUSH：运营版块</li>
 * <li>TAB：分页签Tabs</li>
 * </ul>
 */
public enum PortalItemGroupWidgetStyle {

    DEFAULT("Default"),
    NAVIGATOR_METRO("Metro"),
    NAVIGATOR_LIGHT("Light"),
    NAVIGATOR_GLLERY("Gallery"),
    BULLETINS("Bulletins"),
    OPPUSH("OPPush"),
    TAB("Tab");

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
