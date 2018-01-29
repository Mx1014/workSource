package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>identifierToken: 用户手机号</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>size: size</li>
 * </ul>
 */
public class SearchUserByIdentifierCommand {
    @NotNull
    private String identifierToken;
    @NotNull
    private Integer namespaceId;

    private Integer size;

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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
