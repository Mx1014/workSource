// @formatter:off
package com.everhomes.rest.oauth2client;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>status: 第三方响应状态吗， 200, 400, 404, 500</li>
 *     <li>response: 第三方返回的结果</li>
 *     <li>contentType: 第三方结果类型，从响应的头中获取。application/json, text/plain, application/xml</li>
 * </ul>
 */
public class OAuth2ClientApiResponse {

    private Integer status;
    private String response;
    private String contentType;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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
