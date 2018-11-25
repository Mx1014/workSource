// @formatter:off
package com.everhomes.user;

import com.everhomes.namespace.Namespace;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.util.StringHelper;

import java.util.Locale;

public class User extends EhUsers {
    private static final long serialVersionUID = 7192442994996415975L;
    
    private String identifierToken;
    private Long impersonationUserId;
    
    /** 暂认为小于10的用户为系统用户，系统用户不接收消息，当系统用户数量超过限制需要扩充时需要修改此最大值，否则消息那边的控制有问题 */
    public static final long MAX_SYSTEM_USER_ID = 10;

    public static final long ROOT_UID = 1L;
    
    public static final long SYSTEM_UID = 2L;
    
    public static final long ANNONYMOUS_UID = 0L;
    
    public static final long BIZ_UID = 3L;
    
    public static final UserLogin SYSTEM_USER_LOGIN = new UserLogin(0, User.SYSTEM_UID, 0, "system", null, "0.0.0");
    
    public static final UserLogin ANNONYMOUS_LOGIN = new UserLogin(0, User.ANNONYMOUS_UID, 0, null, null, "0.0.0");
    
    public static final UserLogin BIZ_USER_LOGIN = new UserLogin(0, User.BIZ_UID, 0, "bizhelper", null, "0.0.0");
    
    
    public User() {
    }
    
    public User(Long id) {
		super();
		setId(id);
	}

	public String getLocale() {
        String locale = super.getLocale();
        if(locale == null || locale.trim().length() == 0) {
            locale = Locale.SIMPLIFIED_CHINESE.toString();
        }
        
        return locale;
    }
    
    public Integer getNamespaceId() {
        if(super.getNamespaceId() == null) {
            return Namespace.DEFAULT_NAMESPACE;
        } else {
            return super.getNamespaceId();
        }
    }
    
    public String getIdentifierToken() {
		return identifierToken;
	}

	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
	}

	public Long getImpersonationUserId() {
        return impersonationUserId;
    }

    public void setImpersonationUserId(Long impersonationUserId) {
        this.impersonationUserId = impersonationUserId;
    }

    public void incrementVersion() {
        synchronized (this.getId()) {
            this.setUpdateVersion(this.getUpdateVersion() + 1);
        }
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
