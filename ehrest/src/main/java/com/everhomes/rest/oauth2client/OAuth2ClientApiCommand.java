// @formatter:off
package com.everhomes.rest.oauth2client;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>url: 第三方接口url</li>
 *     <li>param: 请求参数</li>
 *     <li>method: 请求方式 GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE;</li>
 *     <li>contentType: 请求的参数类型 application/json, text/plain</li>
 * </ul>
 */
public class OAuth2ClientApiCommand {

    private String url;
    private String param;
    private String method;
    private String contentType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
