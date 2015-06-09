// @formatter:off
package com.everhomes.banner;


/**
 * <ul>参数类型
 * <li>COUNTRY: 全国</li>
 * <li>CITY: 城市</li>
 * <li>COMMUNITY: 小区</li>
 * </ul>
 */
public enum BannerScopeType {
    COUNTRY("country"),CITY("city"), COMMUNITY("community");
    
 private String code;
    
    private BannerScopeType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static BannerScopeType fromCode(String code) {
        if(code == null)
            return null;

        if(code.equalsIgnoreCase(COUNTRY.getCode()))
            return COUNTRY;
        else if(code.equalsIgnoreCase(CITY.getCode()))
            return CITY;
        else if(code.equalsIgnoreCase(COMMUNITY.getCode()))
            return COMMUNITY;
        
        return null;
    }
}
