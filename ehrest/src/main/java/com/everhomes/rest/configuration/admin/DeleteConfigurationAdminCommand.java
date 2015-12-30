package com.everhomes.rest.configuration.admin;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: 配置项名称</li>
 * </ul>
 */
public class DeleteConfigurationAdminCommand {
    
    @NotNull
    private String name;

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
