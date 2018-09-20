package com.everhomes.eureka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class DataCenterInfo {

    enum Name {Netflix, Amazon, MyOwn}

    @SerializedName("@class")
    private String clz = "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo";
    private final Name name;

    @JsonCreator
    public DataCenterInfo(@JsonProperty("name") Name name) {
        this.name = name;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    public Name getName() {
        return name;
    }
}
