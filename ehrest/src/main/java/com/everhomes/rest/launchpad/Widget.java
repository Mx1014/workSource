// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>组内控件类型
 * <li>NAVIGATOR：导航</li>
 * <li>NAVIGATORTEMP：临时组件，用于标准版电商</li>
 * <li>BANNERS: banner</li>
 * <li>COUPONS: 优惠券</li>
 * <li>POSTS：帖</li>
 * <li>ACTIONBARS：工具栏</li>
 * <li>CALLPHONES：电话组件</li>
 * <li>BULLETINS：滚动广告组件</li>
 * <li>NEWS：园区快讯</li>
 * <li>NEWS_FLASH：园区快讯新样式</li>
 * <li>OPPUSH：首页运营</li>
 * <li>TAB：Tab类型样式</li>
 * <li>CARD：卡片式icon容器</li>
 * <li>CARDEXTENSION：运营卡片</li>
 * </ul>
 */
public enum Widget {
    NAVIGATOR("Navigator"),
    NAVIGATORTEMP("NavigatorTemp"),
    BANNERS("Banners"),
    COUPONS("Coupons"),
    POSTS("Posts"),
    ACTIONBARS("ActionBars"),
    CALLPHONES("CallPhones"),
    NEWS("News"),
    NEWS_FLASH("NewsFlash"),
    BULLETINS("Bulletins"),
    OPPUSH("OPPush"),
	TAB("Tab"),
    CARD("Card"),
    CARDEXTENSION("CardExtension");

    private String code;

       private Widget(String code) {
           this.code = code;
       }

       public String getCode() {
           return this.code;
       }

       public static Widget fromCode(String code) {
           Widget[] values = Widget.values();
           for(Widget value : values) {
               if(value.code.equals(code)) {
                   return value;
               }
           }

           return null;
       }
}
