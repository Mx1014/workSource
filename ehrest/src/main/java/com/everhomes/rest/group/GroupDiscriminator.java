// @formatter:off
package com.everhomes.rest.group;

public enum GroupDiscriminator {
    ENTERPRISE("enterprise"), GROUP("group"), FAMILY("family"), COMMUNITY_BULLETIN("communityBulletin");

    private String code;
    
    private GroupDiscriminator(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static GroupDiscriminator fromCode(String code) {
        if(code != null) {
            for(GroupDiscriminator value : GroupDiscriminator.values()) {
                if(code.equalsIgnoreCase(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
