// @formatter:off
package com.everhomes.launchpad;


/**
 * <ul>参数类型
 * <li>SYS_BANNERS: 首页banner</li>
 * <li>SYS_GOVAGENCIES: 政府机构</li>
 * <li>SYS_COUPONS: 优惠券</li>
 * <li>SYS_BIZS: 商家</li>
 * <li>SYS_PM_BANNERS: 物业banner</li>
 * <li>SYS_PM_ACTIONS: 物业action</li>
 * <li>SYS_PM_TOPICS: 物业topic</li>
 * </ul>
 */
public enum ItemTag {
    SYS_BANNERS("/System/Banners"),SYS_GOVAGENCIES("/System/GovAgencies"), SYS_COUPONS("/System/Coupons"), 
    SYS_BIZS("/System/Bizs"),SYS_PM_BANNERS("/System/Pm/Banners"),SYS_PM_ACTIONS("/System/Pm/Actions"),
    SYS_PM_TOPICS("/System/Pm/Topics");
    
 private String code;
    
    private ItemTag(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ItemTag fromCode(String code) {
        if(code == null)
            return null;

        if(code.equalsIgnoreCase(SYS_BANNERS.getCode()))
            return SYS_BANNERS;
        else if(code.equalsIgnoreCase(SYS_GOVAGENCIES.getCode()))
            return SYS_GOVAGENCIES;
        else if(code.equalsIgnoreCase(SYS_COUPONS.getCode()))
            return SYS_COUPONS;
        else if(code.equalsIgnoreCase(SYS_BIZS.getCode()))
            return SYS_BIZS;
        else if(code.equalsIgnoreCase(SYS_PM_BANNERS.getCode()))
            return SYS_PM_BANNERS;
        else if(code.equalsIgnoreCase(SYS_PM_ACTIONS.getCode()))
            return SYS_PM_ACTIONS;
        else if(code.equalsIgnoreCase(SYS_PM_TOPICS.getCode()))
            return SYS_PM_TOPICS;
        
        return null;
    }
}
