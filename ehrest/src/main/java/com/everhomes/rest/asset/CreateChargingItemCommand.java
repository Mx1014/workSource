//@formatter:off
package com.everhomes.rest.asset;

import java.math.BigDecimal;

/**
 * @author created by ycx
 * @date 下午9:01:20
 */

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>namespaceId: 域名</li>
 * <li>categoryId: 多入口id</li>
 * <li>chargingItemName: 收费项名称</li>
 * <li>projectLevelName: 费项显示名称</li>
 * <li>taxRate: 税率</li>
 *</ul>
 */
public class CreateChargingItemCommand {
    private Long ownerId;
    private String ownerType;
    private Integer namespaceId;
    private Long categoryId;
    private String chargingItemName;
    private String projectLevelName;
    private BigDecimal taxRate;
    private Long appId;
    private Long organziationId;

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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getChargingItemName() {
		return chargingItemName;
	}
	public void setChargingItemName(String chargingItemName) {
		this.chargingItemName = chargingItemName;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public String getProjectLevelName() {
		return projectLevelName;
	}
	public void setProjectLevelName(String projectLevelName) {
		this.projectLevelName = projectLevelName;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getOrganziationId() {
		return organziationId;
	}

	public void setOrganziationId(Long organziationId) {
		this.organziationId = organziationId;
	}
}
