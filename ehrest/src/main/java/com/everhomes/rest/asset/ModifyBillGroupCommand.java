//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>BillGroupId:账单组id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>billingCycle:生成账单周期,2:按月;3:按季度;4:按年</li>
 * <li>billDay:出账单日</li>
 * <li>dueDay:最晚还款日</li>
 * <li>dueDayType:最晚还款日的单位类型，1:日; 2:月</li>
 * <li>bizPayeeAccount:收款方账户名称</li>
 * <li>bizPayeeType:收款方账户类型：EhUsers/EhOrganizations</li>
 * <li>bizPayeeId:收款方账户id</li>
 * <li>organizationId: 标准版新增的管理公司ID</li>
 * <li>allScope: 标准版增加的allScope参数，true：默认/全部，false：具体项目</li>
 * <li>appId:应用ID</li>
 * <li>billingCycleExpression:生成账单周期表达式</li>
 * <li>billDayExpression:出账单日表达式</li>
 * <li>dueDayExpression:最晚还款日表达式</li>
 *</ul>
 */
public class ModifyBillGroupCommand {
    @NotNull
    private Long BillGroupId;
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
    private Byte billDayType;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long categoryId;
    
    private String bizPayeeAccount;
    private String bizPayeeType;
    private String bizPayeeId;
    
    private Long organizationId;//标准版新增的管理公司ID
    private Boolean allScope;//标准版增加的allScope参数，true：默认/全部，false：具体项目
    private Long appId;

    //生成账单周期表达式
    private String billingCycleExpression;
    //出账单日表达式
    private String billDayExpression;
    //最晚还款日表达式
    private String dueDayExpression;

    public String getBillingCycleExpression() {
        return billingCycleExpression;
    }

    public void setBillingCycleExpression(String billingCycleExpression) {
        this.billingCycleExpression = billingCycleExpression;
    }

    public String getBillDayExpression() {
        return billDayExpression;
    }

    public void setBillDayExpression(String billDayExpression) {
        this.billDayExpression = billDayExpression;
    }

    public String getDueDayExpression() {
        return dueDayExpression;
    }

    public void setDueDayExpression(String dueDayExpression) {
        this.dueDayExpression = dueDayExpression;
    }

    public Byte getBillDayType() {
        return billDayType;
    }

    public void setBillDayType(Byte billDayType) {
        this.billDayType = billDayType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupId() {
        return BillGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        BillGroupId = billGroupId;
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

    public ModifyBillGroupCommand() {

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getAllScope() {
		return allScope;
	}

	public void setAllScope(Boolean allScope) {
		this.allScope = allScope;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
