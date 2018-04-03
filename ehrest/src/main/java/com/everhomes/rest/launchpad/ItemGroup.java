// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>参数类型
 * <li>DEFAULT(Default)：默认组</li>
 * <li>GOVAGENCIES(GovAgencies): 首页政府部门（物业，公安，居委等）</li>
 * <li>BIZS(Bizs): 商家</li>
 * <li>GAACTIONS(GaActions): 政府相关，发帖动作（报修，投诉，建议，缴费等）</li>
 * <li>ACTIONBARS(ActionBars): 工具栏</li>
 * <li>CALLPHONES(CallPhones): 电话</li>
 * <li>PAYACTIONS(PayActions): 缴费</li>
 * <li>COUPONS(Coupons): 优惠劵</li>
 * <li>GAPOSTS(GaPosts): 分类查帖</li>
 * <li>OPPUSHACTIVITY(OPPushActivity): 活动运营</li>
 * <li>OPPUSHBIZ(OPPushBiz): 电商运营</li>
 * <li>GALLERY("Gallery"): 项目大图列表（服务联盟等）</li>
 * </ul>
 */
public enum ItemGroup {
    DEFAULT("Default"),GOVAGENCIES("GovAgencies"),BIZS("Bizs"),GAACTIONS("GaActions"),ACTIONBARS("ActionBars"),CALLPHONES("CallPhones"),
    PAYACTIONS("PayActions"),COUPONS("Coupons"),GAPOSTS("GaPosts"), OPPUSHACTIVITY("OPPushActivity"), OPPUSHBIZ("OPPushBiz"), GALLERY("Gallery");
    
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
