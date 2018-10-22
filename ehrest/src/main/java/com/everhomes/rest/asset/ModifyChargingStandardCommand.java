//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>chargingStandardId:收费标准id</li>
 * <li>chargingStandardName:收费标准名称</li>
 * <li>instruction:说明</li>
 * <li>suggestUnitPrice:建议单价</li>
 * <li>areaSizeType:计费面积类型</li>
 * <li>categoryId: 缴费多应用ID</li>
 * <li>organizationId:管理公司ID</li>
 * <li>appId:应用ID</li>
 * <li>allScope:标准版增加的allScope参数，true：默认/全部，false：具体项目</li>
 *</ul>
 */
public class ModifyChargingStandardCommand {
    private Long chargingStandardId;
    private String chargingStandardName;
    private String instruction;
    private BigDecimal suggestUnitPrice;
    private Integer areaSizeType;
    private Long ownerId;
    private String ownerType;
    private Integer namespaceId;
    private Byte useUnitPrice;
    
    private Long categoryId;
    private Long organizationId;
    private Long appId;
    private Boolean allScope;//标准版增加的allScope参数，true：默认/全部，false：具体项目

    public Byte getUseUnitPrice() {
        return useUnitPrice;
    }

    public void setUseUnitPrice(Byte useUnitPrice) {
        this.useUnitPrice = useUnitPrice;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public ModifyChargingStandardCommand() {
    }


    public BigDecimal getSuggestUnitPrice() {
        return suggestUnitPrice;
    }

    public void setSuggestUnitPrice(BigDecimal suggestUnitPrice) {
        this.suggestUnitPrice = suggestUnitPrice;
    }

    public Integer getAreaSizeType() {
        return areaSizeType;
    }

    public void setAreaSizeType(Integer areaSizeType) {
        this.areaSizeType = areaSizeType;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }



    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public String getChargingStandardName() {
        return chargingStandardName;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setChargingStandardName(String chargingStandardName) {
        this.chargingStandardName = chargingStandardName;
    }

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
