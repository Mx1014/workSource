package com.everhomes.rest.openapi;

import com.everhomes.util.StringHelper;


public class OrganizationDTO {
    private Long id;
    private String name;
    private String groupType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
        
    }
}
