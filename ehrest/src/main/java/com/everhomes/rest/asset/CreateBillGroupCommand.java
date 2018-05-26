//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者type</li>
 * <li>billGroupName:账单组名称</li>
 * <li>billingCycle:生成账单周期,2:按月;3:按季度;4:按年</li>
 * <li>billDay:出账单日</li>
 * <li>dueDay:最晚还款日</li>
 * <li>dueDayType:最晚还款日的单位类型，1:日; 2:月</li>
 * <li>bizPayeeAccount:收款方账户名称</li>
 * <li>bizPayeeType:收款方账户类型：EhUsers/EhOrganizations</li>
 * <li>bizPayeeId:收款方账户id</li>
 *</ul>
 */
public class CreateBillGroupCommand {
    @NotNull
    private Integer namespaceId;
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    @NotNull
    private String billGroupName;
    @NotNull
    private Byte billingCycle;
    @NotNull
    private Integer billDay;
    @NotNull
    private Integer dueDay;
    @NotNull
    private Byte dueDayType;
    private String bizPayeeAccount;
    private String bizPayeeType;
    private String bizPayeeId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Integer getBillDay() {
        return billDay;
    }

    public void setBillDay(Integer billDay) {
        this.billDay = billDay;
    }

    public Integer getDueDay() {
        return dueDay;
    }

    public void setDueDay(Integer dueDay) {
        this.dueDay = dueDay;
    }

    public Byte getDueDayType() {
        return dueDayType;
    }

    public void setDueDayType(Byte dueDayType) {
        this.dueDayType = dueDayType;
    }

    public CreateBillGroupCommand() {

    }

	public String getBizPayeeAccount() {
		return bizPayeeAccount;
	}

	public void setBizPayeeAccount(String bizPayeeAccount) {
		this.bizPayeeAccount = bizPayeeAccount;
	}

	public String getBizPayeeType() {
		return bizPayeeType;
	}

	public void setBizPayeeType(String bizPayeeType) {
		this.bizPayeeType = bizPayeeType;
	}

	public String getBizPayeeId() {
		return bizPayeeId;
	}

	public void setBizPayeeId(String bizPayeeId) {
		this.bizPayeeId = bizPayeeId;
	}
}
