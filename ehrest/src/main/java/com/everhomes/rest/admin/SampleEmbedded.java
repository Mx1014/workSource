package com.everhomes.rest.admin;

import com.everhomes.util.StringHelper;

public class SampleEmbedded {
    private String name;
    private String value;
    
    public SampleEmbedded() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
