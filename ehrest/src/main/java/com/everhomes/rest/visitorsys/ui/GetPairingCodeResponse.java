// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
  *<ul>
  *<li>1 : 返回码200-成功，10001-已注册过，可以直接获取配置，10002-配对码失效，10003-其他错误</li>
  *<li>pairingCode : (必填)配对码</li>
  *<li>pairingCodelive : (必填)有效时长(单位:秒)</li>
  *<li>vaildTime : (必填)配对码有效时间</li>
  *</ul>
  */

public class GetPairingCodeResponse {
    private String pairingCode;
    private Integer pairingCodelive;
    private Timestamp vaildTime;

    public String getPairingCode() {
        return pairingCode;
    }

    public void setPairingCode(String pairingCode) {
        this.pairingCode = pairingCode;
    }

    public Integer getPairingCodelive() {
        return pairingCodelive;
    }

    public void setPairingCodelive(Integer pairingCodelive) {
        this.pairingCodelive = pairingCodelive;
    }

    public Timestamp getVaildTime() {
        return vaildTime;
    }

    public void setVaildTime(Timestamp vaildTime) {
        this.vaildTime = vaildTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
