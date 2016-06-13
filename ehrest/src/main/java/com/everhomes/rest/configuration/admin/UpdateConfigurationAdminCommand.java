package com.everhomes.rest.configuration.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: 配置项名称</li>
 * <li>value: 配置项取值</li>
 * </ul>
 */
public class UpdateConfigurationAdminCommand {

    @NotNull
    private String name;
    private String value;
    private Integer namespaceId;

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
