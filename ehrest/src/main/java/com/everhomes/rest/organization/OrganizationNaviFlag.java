package com.everhomes.rest.organization;

/**
 * <p>机构是否导航</p>
 * <ul>
 * <li>HIDE_NAVI: 不展示在导航</li>
 * <li>SHOW_NAVI: 展示在导航</li>
 * </ul>
 */
public enum OrganizationNaviFlag {
	
	HIDE_NAVI((byte)0), SHOW_NAVI((byte)1);
    
    private byte code;
    
    private OrganizationNaviFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationNaviFlag fromCode(Byte code) {
        if(code != null) {
            for(OrganizationNaviFlag value : OrganizationNaviFlag.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}