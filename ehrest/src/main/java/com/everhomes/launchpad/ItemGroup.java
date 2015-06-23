// @formatter:off
package com.everhomes.launchpad;

/**
 * <ul>参数类型
 * <li>DEFAULT：默认组</li>
 * <li>GOVAGENCIES: 首页政府部门（物业，公安，居委等）</li>
 * <li>BIZS: 商家</li>
 * <li>GAACTIONS: 政府相关，发帖动作（报修，投诉，建议，缴费等）</li>
 * </ul>
 */
public enum ItemGroup {
    DEFAULT("Default"),GOVAGENCIES("GovAgencies"),BIZS("Bizs"),GAACTIONS("GaActions");
    
    private String code;
       
       private ItemGroup(String code) {
           this.code = code;
       }
       
       public String getCode() {
           return this.code;
       }
       
       public static ItemGroup fromCode(String code) {
           ItemGroup[] values = ItemGroup.values();
           for(ItemGroup value : values) {
               if(value.code.equals(code)) {
                   return value;
               }
           }
           
           return null;
       }
}
