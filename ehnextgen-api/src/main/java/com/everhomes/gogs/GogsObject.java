package com.everhomes.gogs;

import com.everhomes.util.StringHelper;

import java.util.Date;

public class GogsObject {

    private String id;
    private String name;
    private Long size;
    private String objectType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
