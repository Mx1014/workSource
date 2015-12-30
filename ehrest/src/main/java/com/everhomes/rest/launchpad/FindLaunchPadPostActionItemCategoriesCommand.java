// @formatter:off
package com.everhomes.rest.launchpad;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>itemLocation: item的路径</li>
 *  <li>itemGroup: 当前item归属哪个组</li>
 * <li>communityId: 小区id</li>
 * </ul>
 */
public class FindLaunchPadPostActionItemCategoriesCommand {
    
    @NotNull
    private String    itemLocation;
    @NotNull
    private String    itemGroup;
    @NotNull
    private Long    communityId;

    public FindLaunchPadPostActionItemCategoriesCommand() {
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
