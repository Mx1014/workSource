// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId: 家庭Id</li>
 * </ul>
 */
public class SetFollowedFamilyAliasCommand {
    private Long familyId;
    private String aliasName;

    public SetFollowedFamilyAliasCommand() {
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
