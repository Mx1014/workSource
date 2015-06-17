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
public class GetLaunchPadItemsByTagCommand {
    
    @NotNull
    private String    tag;
    @NotNull
    private Long    communityId;

    public GetLaunchPadItemsByTagCommand() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
