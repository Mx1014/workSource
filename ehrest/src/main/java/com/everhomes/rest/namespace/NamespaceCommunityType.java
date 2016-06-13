// @formatter:off
package com.everhomes.rest.namespace;

/**
 * <ul>一个域空间下所管辖的小区，有可能都是住宅小区、有可能都是商业小区、还有可能是混合的
 * <li>COMMUNITY_RESIDENTIAL(community_residential): 住宅小区</li>
 * <li>COMMUNITY_COMMERCIAL(community_commercial): 商业小区（园区）</li>
 * <li>COMMUNITY_MIX(community_mix): 住宅与商业小区混合</li>
 * </ul>
 */
public enum NamespaceCommunityType {
    COMMUNITY_RESIDENTIAL("community_residential"), COMMUNITY_COMMERCIAL("community_commercial"), COMMUNITY_MIX("community_mix");
	
    private String code;
    
    private NamespaceCommunityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

	public static NamespaceCommunityType fromCode(String code) {
        NamespaceCommunityType[] values = NamespaceCommunityType.values();
        for(NamespaceCommunityType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }

}
