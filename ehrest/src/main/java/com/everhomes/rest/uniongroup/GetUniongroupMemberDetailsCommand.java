package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/6/29.
 */
public class GetUniongroupMemberDetailsCommand {

    private Long groupId;

    public GetUniongroupMemberDetailsCommand() {

    }

    public GetUniongroupMemberDetailsCommand(Long groupId) {
        super();
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
