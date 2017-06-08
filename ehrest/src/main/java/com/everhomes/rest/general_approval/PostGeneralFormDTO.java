package com.everhomes.rest.general_approval;


import com.everhomes.util.StringHelper;

public class PostGeneralFormDTO {
    private String customObject;

    public String getCustomObject() {
        return customObject;
    }

    public void setCustomObject(String customObject) {
        this.customObject = customObject;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
