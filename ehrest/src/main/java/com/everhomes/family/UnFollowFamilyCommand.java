// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId: 关注的家庭Id</li>
 * </ul>
 */
public class UnFollowFamilyCommand {
    private Long familyId;

    public UnFollowFamilyCommand() {
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
