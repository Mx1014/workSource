// @formatter:off
package com.everhomes.rest.oauth2client;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>vendor: 提供商</li>
 *     <li>clientId: 第三方提供</li>
 *     <li>clientSecret: 第三方提供</li>
 *     <li>redirectUri: 在第三方注册</li>
 *     <li>responseType: 响应类型</li>
 *     <li>grantType: grantType</li>
 *     <li>state: state</li>
 *     <li>scope: scope</li>
 *     <li>authorizeUrl: 第三方登录地址</li>
 *     <li>tokenUrl: 第三方获取token地址</li>
 * </ul>
 */
public class OAuth2ServerDTO {

    private Long id;
    private String vendor;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String responseType;
    private String grantType;
    private String state;
    private String scope;
    private String authorizeUrl;
    private String tokenUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
}
