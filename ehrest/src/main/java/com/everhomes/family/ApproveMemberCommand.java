// @formatter:off
package com.everhomes.family;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.family.BaseCommand}</li>
 * <li>memberUid: 成员Id</li>
 * </ul>
 */
public class ApproveMemberCommand extends BaseCommand{
    @NotNull
    private Long memberUid;

    public ApproveMemberCommand() {
    }
    
    public Long getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(Long memberUid) {
        this.memberUid = memberUid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
