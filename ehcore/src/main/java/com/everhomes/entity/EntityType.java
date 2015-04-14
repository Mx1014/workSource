// @formatter:off
package com.everhomes.entity;

import com.everhomes.schema.tables.EhForums;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.server.schema.tables.pojos.EhUsers;

public enum EntityType {
    USER(EhUsers.class.getSimpleName()),
    GROUP(EhGroups.class.getSimpleName()),
    FORUM(EhForums.class.getSimpleName()),
    ADDRESS(EhAddresses.class.getSimpleName()),
    CATEGORY(EhCategories.class.getSimpleName()),
    COMMUNITY(EhCommunities.class.getSimpleName()),
    REGION(EhRegions.class.getSimpleName()),
    FAMILY("EhFamilies");
    
    private String code;
    
    private EntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static EntityType fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase(USER.getCode()))
            return USER;
        else if(code.equalsIgnoreCase(GROUP.getCode()))
            return GROUP;
        else if(code.equalsIgnoreCase(FORUM.getCode()))
            return FORUM;
        else if(code.equalsIgnoreCase(ADDRESS.getCode()))
            return ADDRESS;
        else if(code.equalsIgnoreCase(CATEGORY.getCode()))
            return CATEGORY;
        else if(code.equalsIgnoreCase(COMMUNITY.getCode()))
            return COMMUNITY;
        else if(code.equalsIgnoreCase(REGION.getCode()))
            return REGION;
        else if(code.equalsIgnoreCase(FAMILY.getCode()))
            return FAMILY;
        
        return null;
    }
}
