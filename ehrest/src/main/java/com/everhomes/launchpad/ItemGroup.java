// @formatter:off
package com.everhomes.launchpad;

/**
 * <ul>参数类型
 * <li>PROPERTY: 物业</li>
 * <li>COUPON: 优惠券</li>
 * <li>THIRDSERVICE: 第三方服务</li>
 * </ul>
 */
public enum ItemGroup {
    PROPERTY("Property"),COUPON("Coupon"),THIRDSERVICE("ThirdService");
    
    private String code;
       
       private ItemGroup(String code) {
           this.code = code;
       }
       
       public String getCode() {
           return this.code;
       }
       
       public static ItemGroup fromCode(String code) {
           if(code == null)
               return null;

           if(code.equalsIgnoreCase(PROPERTY.getCode()))
               return PROPERTY;
           else if(code.equalsIgnoreCase(COUPON.getCode()))
               return COUPON;
           else if(code.equalsIgnoreCase(THIRDSERVICE.getCode()))
               return THIRDSERVICE;
           
           return null;
       }
}
