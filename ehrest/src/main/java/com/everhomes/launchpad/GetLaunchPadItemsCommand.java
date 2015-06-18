// @formatter:off
package com.everhomes.launchpad;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>tag: 标签类型</li>
 * <li>communityId: 小区id</li>
 * </ul>
 */
public class GetLaunchPadItemsCommand {
    
    @NotNull
    private String    componentTag;
    @NotNull
    private Long    communityId;

    public GetLaunchPadItemsCommand() {
    }

    public String getComponentTag() {
        return componentTag;
    }

    public void setComponentTag(String componentTag) {
        this.componentTag = componentTag;
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
