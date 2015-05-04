// @formatter:off
package com.everhomes.entity;

import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.server.schema.tables.pojos.EhForums;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.server.schema.tables.pojos.EhUsers;

/**
 * <p>实体类型:</p>
 * <ul>
 * <li>USER：用户</li>
 * <li>GROUP: 组</li>
 * <li>FORUM: 论坛</li>
 * <li>ADDRESS: 地址</li>
 * <li>ADDRESS: 地址</li>
 * <li>CATEGORY: 类型</li>
 * <li>COMMUNITY: 小区</li>
 * <li>REGION: 区域</li>
 * <li>POST: 帖子/评论</li>
 * <li>FAMILY: 家庭</li>
 * <li>FLEAMARKET: 跳蚤市场</li>
 * </ul>
 */
public enum EntityType {
    USER(EhUsers.class.getSimpleName()),
    GROUP(EhGroups.class.getSimpleName()),
    FORUM(EhForums.class.getSimpleName()),
    ADDRESS(EhAddresses.class.getSimpleName()),
    CATEGORY(EhCategories.class.getSimpleName()),
    COMMUNITY(EhCommunities.class.getSimpleName()),
    REGION(EhRegions.class.getSimpleName()),
    POST(EhForumPosts.class.getSimpleName()),
    
    FAMILY("EhFamilies"),
    FLEAMARKET("EhFleaMarkets");
    
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
        else if(code.equalsIgnoreCase(POST.getCode()))
            return POST;
        else if(code.equalsIgnoreCase("EhFamilies"))
            return FAMILY;
        else if(code.equalsIgnoreCase("EhFleaMarkets"))
            return FLEAMARKET;
        
        return null;
    }
}
