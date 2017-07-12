// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>item类型
 * <li>DEFAULT(default)：默认</li>
 * <li>BIZ(biz): 商家</li>
 * </ul>
 */
public enum ItemTargetType {
    DEFAULT("default"),BIZ("biz"), ZUOLIN_SHOP("zuolin_shop");
    
    private String code;
       
       private ItemTargetType(String code) {
           this.code = code;
       }
       
       public String getCode() {
           return this.code;
       }
       
       public static ItemTargetType fromCode(String code) {
           ItemTargetType[] values = ItemTargetType.values();
           for(ItemTargetType value : values) {
               if(value.code.equals(code)) {
                   return value;
               }
           }
           
           return null;
       }
}
