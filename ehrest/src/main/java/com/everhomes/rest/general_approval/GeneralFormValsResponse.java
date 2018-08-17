package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

import java.util.Map;

public class GeneralFormValsResponse {

    private Map<String , Object> valList;

    public Map<String, Object> getValList() {
        return valList;
    }

    public void setValList(Map<String, Object> valList) {
        this.valList = valList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
