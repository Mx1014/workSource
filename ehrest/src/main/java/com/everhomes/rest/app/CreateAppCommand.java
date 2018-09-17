package com.everhomes.rest.app;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>name: name</li>
 *     <li>description: description</li>
 *     <li>appKey: appKey</li>
 *     <li>secretKey: secretKey</li>
 * </ul>
 */
public class CreateAppCommand {

    private Integer namespaceId;
    private String name;
    private String description;
    private String appKey;
    private String secretKey;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
