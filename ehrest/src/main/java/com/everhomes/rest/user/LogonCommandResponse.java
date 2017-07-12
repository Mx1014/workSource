// @formatter:off
package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 登录成功后的响应
 * @author elians
 *<ul>
 *<li>uid:用户ID</li>
 *<li>loginToken:登录成功令牌</li>
 *<li>contentServer:内容服务器</li>
 *<li>accessPoints:消息服务其</li>
 *</ul>
 */
public class LogonCommandResponse {
    private long uid;
    private String loginToken;
    private String contentServer;

    @ItemType(String.class)
    private List<String> accessPoints;
    
    public LogonCommandResponse() {
    }
    
    public LogonCommandResponse(long uid, String loginToken) {
        this.uid = uid;
        this.loginToken = loginToken;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
    
    public String getContentServer() {
        return contentServer;
    }
    
    public void setContentServer(String contentServer) {
        this.contentServer = contentServer;
    }
    
    public List<String> getAccessPoints() {
        return this.accessPoints;
    }
    
    public void setAccessPoints(List<String> accessPoints) {
        this.accessPoints = accessPoints;
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
