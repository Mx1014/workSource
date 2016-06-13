// @formatter:off
package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * 登录
 * @author elians
 *<ul>
 *<li>userIdentifier:用户标识，目前手机号和邮箱支持</li>
 *</ul>
 */
public class UserTokenCommand {
    @NotNull
    private String userIdentifier;
   
    private Integer namespaceId;
    
    public UserTokenCommand() {
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
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
