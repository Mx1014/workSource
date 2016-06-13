// @formatter:off
package com.everhomes.rest.namespace;

/**
 * <ul>
 * <li>COMMUNITY: 小区</li>
 * </ul>
 */
public enum NamespaceResourceType {
	COMMUNITY("COMMUNITY");
    
    private String code;
    
    private NamespaceResourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

	public static NamespaceResourceType fromCode(String code) {
        NamespaceResourceType[] values = NamespaceResourceType.values();
        for(NamespaceResourceType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }

}
