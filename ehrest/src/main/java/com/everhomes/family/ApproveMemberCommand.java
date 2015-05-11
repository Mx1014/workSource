// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId: 家庭Id</li>
 * <li>memberUid: 成员Id</li>
 * </ul>
 */
public class ApproveMemberCommand {
    private Long familyId;
    private Long memberUid;

    public ApproveMemberCommand() {
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
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
