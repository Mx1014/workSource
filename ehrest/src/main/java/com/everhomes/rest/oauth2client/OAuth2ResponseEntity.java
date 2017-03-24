package com.everhomes.rest.oauth2client;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>access_token：表示访问令牌，必选项。</li>
 *     <li>token_type：表示令牌类型，该值大小写不敏感，必选项，可以是bearer类型或mac类型。</li>
 *     <li>expires_in：表示过期时间，单位为秒。如果省略该参数，必须其他方式设置过期时间。</li>
 *     <li>refresh_token：表示更新令牌，用来获取下一次的访问令牌，可选项。</li>
 *     <li>scope：表示权限范围，如果与客户端申请的范围一致，此项可省略。</li>
 * </ul>
 */
public class OAuth2ResponseEntity {

    private String access_token;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
