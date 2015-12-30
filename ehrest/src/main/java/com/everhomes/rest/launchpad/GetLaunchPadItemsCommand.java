// @formatter:off
package com.everhomes.rest.launchpad;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>itemLocation: item的路径</li>
 *  <li>itemGroup: 当前item归属哪个组</li>
 * 	<li>communityId: 小区id</li>
 * 	<li>siteUri: 链接</li>
 * </ul>
 */
public class GetLaunchPadItemsCommand {
    
    @NotNull
    private String    itemLocation;
    @NotNull
    private String    itemGroup;
    @NotNull
    private Long    communityId;
    @NotNull
    private Integer namespaceId;

    public GetLaunchPadItemsCommand() {
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

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
