// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>accountType: 帐号类型</li>
 *     <li>accountId:  帐号ID</li>
 *     <li>accountName:  帐号名称</li>
 *     <li>accountAliasName:  帐号别名，如果用户是企业用户则为公司名称，如果是个人用户则为个人名称</li>
 *     <li>accountStatus:  帐号类型，1-未审核、2-已审核
 * </ul>
 */
public class ActivityPayeeDTO {

    private String accountType;

    private Long accountId;

    private String accountName;

    private String accountAliasName;

    private Byte accountStatus;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountAliasName() {
        return accountAliasName;
    }

    public void setAccountAliasName(String accountAliasName) {
        this.accountAliasName = accountAliasName;
    }

    public Byte getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Byte accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
