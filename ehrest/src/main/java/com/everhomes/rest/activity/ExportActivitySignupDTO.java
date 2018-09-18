package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class ExportActivitySignupDTO {

    @ItemType(String.class)
    private List<String> vals;

    public ExportActivitySignupDTO() {
    }

    public List<String> getVals() {
        return vals;
    }

    public void setVals(List<String> vals) {
        this.vals = vals;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
