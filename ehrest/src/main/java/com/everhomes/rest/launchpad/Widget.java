// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>组内控件类型
 * <li>NAVIGATOR：导航</li>
 * <li>BANNERS: banner</li>
 * <li>COUPONS: 优惠券</li>
 * <li>POSTS：帖</li>
 * <li>ACTIONBARS：工具栏</li>
 * <li>CALLPHONES：电话组件</li>
 * </ul>
 */
public enum Widget {
    NAVIGATOR("Navigator"),BANNERS("Banners"),COUPONS("Coupons"),POSTS("Posts"),ACTIONBARS("ActionBars"),CALLPHONES("CallPhones");
    
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
