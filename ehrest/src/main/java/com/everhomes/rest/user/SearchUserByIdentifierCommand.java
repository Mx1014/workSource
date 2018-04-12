package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>identifierToken: 用户手机号</li>
 *     <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class SearchUserByIdentifierCommand {
    @NotNull
    private String identifierToken;
    @NotNull
    private Integer namespaceId;

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
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
