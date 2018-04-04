package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId:项目id</li>
 * <li>communityName:项目名称</li>
 * </ul>
 * Created by rui.jia  2018/1/20 16 :07
 */

public class EquipmentStandardCommunity {
    private Long communityId;
    private String communityName;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
