// @formatter:off
package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 登录成功后的响应
 * @author elians
 *<ul>
 *<li>user:用户信息</li>
 *</ul>
 */
public class UserTokenCommandResponse {
    private UserInfo user;
   
    public UserTokenCommandResponse() {
    }
    

    public UserTokenCommandResponse(UserInfo user) {
		super();
		this.user = user;
	}

    

	public UserInfo getUser() {
		return user;
	}


	public void setUser(UserInfo user) {
		this.user = user;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
