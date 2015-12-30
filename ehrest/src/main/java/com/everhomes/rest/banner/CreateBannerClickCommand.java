package com.everhomes.rest.banner;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>bannerId: banner id</li>
 * <li>familyId: 家庭id</li>
 * </ul>
 */
public class CreateBannerClickCommand {
    @NotNull
    private Long bannerId;
    private Long familyId;

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
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
