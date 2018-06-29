// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>contentServer : (必填)内容服务器地址</li>
  *<li>token : (必填)token</li>
  *</ul>
  */

public class GetUploadFileTokenResponse {
    private String contentServer;
    private String token;

    public GetUploadFileTokenResponse(String contentServer, String token) {
        this.contentServer = contentServer;
        this.token = token;
    }

    public GetUploadFileTokenResponse() {
    }

    public String getContentServer() {
        return contentServer;
    }

    public void setContentServer(String contentServer) {
        this.contentServer = contentServer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
