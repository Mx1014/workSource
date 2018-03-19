package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>communities: 创建园区命令列表参考{@link CreateCommunityCommand}</li>
 **/

public class CreateCommunitiesCommand {
    private List<CreateCommunityCommand> communities;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<CreateCommunityCommand> getCommunities() {
        return communities;
    }

    public void setCommunities(List<CreateCommunityCommand> communities) {
        this.communities = communities;
    }
}
