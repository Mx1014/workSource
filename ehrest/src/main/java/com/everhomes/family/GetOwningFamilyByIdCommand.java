// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId: 家庭Id</li>
 * </ul>
 */
public class GetOwningFamilyByIdCommand {
    private Long familyId;

    public GetOwningFamilyByIdCommand() {
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
