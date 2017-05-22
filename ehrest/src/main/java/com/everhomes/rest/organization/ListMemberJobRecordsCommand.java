package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by R on 2017/5/19.
 * <ul>
 * <li>memberId: 员工编号</li>
 * </ul>
 */
public class ListMemberJobRecordsCommand {

    private Long memberId;

    public ListMemberJobRecordsCommand() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
