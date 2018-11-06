//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>defaultOrder:账单组排序，数值小着优先度高</li>
 * <li>billingCycle:计费周期,1:按天;2:按月;3:按季度;4:按年;</li>
 * <li>billingDay:出账单日</li>
 * <li>dueDay:最晚还款日</li>
 * <li>dueDayType:最晚还款日的单位，1：日；2：月</li>
 * <li>bizPayeeAccount:收款方账户</li>
 * <li>bizPayeeType:收款方账户类型：EhUsers/EhOrganizations</li>
 * <li>bizPayeeId:收款方账户id</li>
 * <li>accountAliasName:收款方名称</li>
 *</ul>
 */
public class ListBillGroupsDTO {
    private Long billGroupId;
    private String billGroupName;
    private Integer defaultOrder;
    private Byte billingCycle;
    private Integer billingDay;
    private Byte billDayType;
    private Integer dueDay;
    private Byte dueDayType;
    
    private String bizPayeeType;
    private Long bizPayeeId;
    private String accountName;
    private String accountAliasName;
    private Byte accountStatus;
    
    private Long brotherGroupId;

    public Byte getBillDayType() {
        return billDayType;
    }

    public void setBillDayType(Byte billDayType) {
        this.billDayType = billDayType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Byte getDueDayType() {
        return dueDayType;
    }

    public void setDueDayType(Byte dueDayType) {
        this.dueDayType = dueDayType;
    }

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Integer getBillingDay() {
        return billingDay;
    }

    public void setBillingDay(Integer billingDay) {
        this.billingDay = billingDay;
    }

    public Integer getDueDay() {
        return dueDay;
    }

    public void setDueDay(Integer dueDay) {
        this.dueDay = dueDay;
    }

    public ListBillGroupsDTO() {

    }

	public String getBizPayeeType() {
		return bizPayeeType;
	}

	public void setBizPayeeType(String bizPayeeType) {
		this.bizPayeeType = bizPayeeType;
	}

	public Long getBizPayeeId() {
		return bizPayeeId;
	}

	public void setBizPayeeId(Long bizPayeeId) {
		this.bizPayeeId = bizPayeeId;
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

	public Long getBrotherGroupId() {
		return brotherGroupId;
	}

	public void setBrotherGroupId(Long brotherGroupId) {
		this.brotherGroupId = brotherGroupId;
	}

	public Long getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}

	public String getBillGroupName() {
		return billGroupName;
	}

	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}

}
