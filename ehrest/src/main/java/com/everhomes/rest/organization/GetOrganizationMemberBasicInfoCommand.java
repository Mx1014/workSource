package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>memberId: 员工编号</li>
 * </ul>
 */
public class GetOrganizationMemberBasicInfoCommand {

    private Long memberId;

    public GetOrganizationMemberBasicInfoCommand() {
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
