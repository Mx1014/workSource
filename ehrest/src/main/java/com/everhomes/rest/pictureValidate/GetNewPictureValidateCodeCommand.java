// @formatter:off
package com.everhomes.rest.pictureValidate;

import com.everhomes.util.StringHelper;

public class GetNewPictureValidateCodeCommand {

    private Integer regionCode;

    private String identifier;

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
