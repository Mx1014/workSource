// @formatter:off
package com.everhomes.launchpad;


/**
 * <ul>参数类型
 * <li>HOME_BANNERS: 首页banner</li>
 * <li>HOME_GOVAGENCIES: 政府机构（物业，公安，居委等）</li>
 * <li>HOME_COUPONS: 优惠券</li>
 * <li>HOME_BIZS: 商家</li>
 * <li>HOME_PM_BANNERS: 物业banner</li>
 * <li>HOME_PM_ACTIONS: 物业发帖动作类型（报修，求诉，建议等）</li>
 * </ul>
 */
public enum ItemPath {
    HOME_BANNERS("/Home/Banners"),HOME_GOVAGENCIES("/Home/GovAgencies"), HOME_COUPONS("/Home/Coupons"), 
    HOME_BIZS("/Home/Bizs"),HOME_PM_BANNERS("/Home/Pm/Banners"),HOME_PM_ACTIONS("/Home/Pm/GaActions");
    
 private String code;
    
    private ItemPath(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ItemPath fromCode(String code) {
        if(code == null)
            return null;

        ItemPath[] values = ItemPath.values();
        for(ItemPath value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
