package com.everhomes.rest.address;

/**
 * 
 * <ul>来自于第三方楼栋的类型，对应eh_addresses表听 namespace_address_type字段
 * <li>JINDIE("jindie"): 金蝶</li>
 * <li>SHENZHOU("shenzhou"): 神州数码</li>
 * <li>EBEI("ebei"): 一碑</li>
 * </ul>
 */
public enum NamespaceAddressType {
	JINDIE("jindie"), SHENZHOU("shenzhou"), EBEI("ebei"), RUIAN_CM("ruian_cm");
    
    private String code;
    private NamespaceAddressType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static NamespaceAddressType fromCode(String code) {
        if(code != null) {
        	NamespaceAddressType[] values = NamespaceAddressType.values();
            for(NamespaceAddressType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
