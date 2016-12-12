package com.everhomes.rest.address;

/**
 * 
 * <ul>来自于第三方楼栋的类型，对应eh_addresses表听 namespace_address_type字段
 * <li>JINDIE("jindie"): 金蝶</li>
 * </ul>
 */
public enum NamespaceAddressType {
	JINDIE("jindie");
    
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
