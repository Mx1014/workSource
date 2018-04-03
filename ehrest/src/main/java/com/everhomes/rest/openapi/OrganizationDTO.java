package com.everhomes.rest.openapi;

import com.everhomes.util.StringHelper;


public class OrganizationDTO {
    private Long id;
    private String name;

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

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
        
    }
}
