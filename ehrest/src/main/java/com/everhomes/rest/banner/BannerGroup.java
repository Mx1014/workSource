// @formatter:off
package com.everhomes.rest.banner;

/**
 * <ul>参数类型
 * <li>DEFAULT: 默认</li>
 * <li>BIZ: 商家</li>
 * <li>PM: 物业</li>
 * <li>GARC: 业委，Government Agency - Resident Committee</li>
 * <li>GANC: 居委，Government Agency - Neighbor Committee</li>
 * <li>GAPS: 公安，Government Agency - Police Station</li>
 * </ul>
 */
public enum BannerGroup {
    DEFAULT("DEFAULT"),GA("GA"),BIZ("BIZ"),PM("PM"),GARC("GARC"),GANC("GANC"),GAPS("GAPS");
    
    private String code;
       
       private BannerGroup(String code) {
           this.code = code;
       }
       
       public String getCode() {
           return this.code;
       }
       
       public static BannerGroup fromCode(String code) {
           BannerGroup[] values = BannerGroup.values();
           for(BannerGroup value : values) {
               if(value.code.equals(code)) {
                   return value;
               }
           }
           
           return null;
       }
}
