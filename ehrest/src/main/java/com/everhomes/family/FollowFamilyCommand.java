// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId: 关注的家庭Id</li>
 * <li>aliasName: 关注家庭的昵称（可选）</li>
 * </ul>
 */
public class FollowFamilyCommand {
    private Long familyId;
    private String aliasName;

    public FollowFamilyCommand() {
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }
    
    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
