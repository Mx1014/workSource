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
 * <li>billingCycle:生成账单周期,参考{@link com.everhomes.rest.asset.BillingCycle}, 账单的周期只支持2及以上</li>
 * <li>billDay:出账单日</li>
 * <li>billDayType:出账单日类型，1. 本周期前几日；2.本周期第几日；3.本周期结束日；4.下周期第几日</li>
 * <li>dueDay:最晚还款日</li>
 * <li>dueDayType:最晚还款日的单位类型，1:日; 2:月</li>
 * <li>bizPayeeType:收款方账户类型：EhUsers/EhOrganizations</li>
 * <li>bizPayeeId:收款方账户id</li>
 * <li>categoryId: 缴费多应用ID</li>
 * <li>organizationId:管理公司ID</li>
 * <li>appId:应用ID</li>
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

    private Integer billDay;
    @NotNull
    private Byte billDayType;
    @NotNull
    private Integer dueDay;
    @NotNull
    private Byte dueDayType;

    private String bizPayeeType;
    private Long bizPayeeId;

    private Long categoryId;

    private Long organizationId;
    private Long appId;
    private Boolean allScope;//标准版增加的allScope参数，true：默认/全部，false：具体项目

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

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

    public Byte getBillDayType() {
        return billDayType;
    }

    public void setBillDayType(Byte billDayType) {
        this.billDayType = billDayType;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

	public Boolean getAllScope() {
		return allScope;
	}

	public void setAllScope(Boolean allScope) {
		this.allScope = allScope;
	}

}
