//@formatter:off
package com.everhomes.rest.asset;

import java.util.Date;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class PaymentAccountResp {
    private String id;
    private String name;
    private Long accountId;
    private Integer systemId;
    private String appKey;
    private String secretKey;
    private Date createTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getSystemId() {
        return systemId;
    }
    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getAppKey() {
        return appKey;
    }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
