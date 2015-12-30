// @formatter:off
package com.everhomes.rest.admin;

import com.everhomes.util.StringHelper;

public class NamespaceDTO {
    private Integer id;
    private String  name;

    public NamespaceDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
