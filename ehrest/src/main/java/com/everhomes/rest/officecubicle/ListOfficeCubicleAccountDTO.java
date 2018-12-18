package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

/**
 * <ul>
 *     <li>accountType: 帐号类型，{@link com.everhomes.rest.order.OwnerType}</li>
 *     <li>accountId:  帐号ID</li>
 *     <li>accountName:  帐号名称</li>
 *     <li>accountAliasName:  帐号别名，如果用户是企业用户则为公司名称，如果是个人用户则为个人名称</li>
 *     <li>accountStatus:  帐号类型，1-未审核、2-已审核，参考{@link com.everhomes.rest.order.PaymentUserStatus}，
 *     对应于支付系统中的com.everhomes.pay.user.BusinessUserDTO的businessCheckResult字段</li>
 * </ul>
 */
public class ListOfficeCubicleAccountDTO implements Serializable{
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
