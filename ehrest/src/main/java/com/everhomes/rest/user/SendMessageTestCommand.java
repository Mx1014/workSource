package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID </li>
 * <li>userId: 用户ID </li>
 * <li>loginId: 用户对应的登录ID </li>
 * </ul>
 * @author janson
 *
 */
public class SendMessageTestCommand {
    @NotNull
    private Integer namespaceId;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private Integer loginId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
