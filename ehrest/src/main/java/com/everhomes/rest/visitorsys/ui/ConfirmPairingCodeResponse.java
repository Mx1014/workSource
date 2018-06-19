// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>appkey : (必填)后续接口调用的appkey</li>
  *<li>secretkey : (必填)参数签名必填</li>
  *</ul>
  */

public class ConfirmPairingCodeResponse {
    private String appkey;
    private String secretkey;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
