package com.everhomes.rest.contract;

import java.math.BigDecimal;

/**
 * Created by djm on 2018/11/19.
 */
public class ContractChargingItemReportformDTO {
    private Integer namespaceId;
    private Long chargingItemId;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
	
    public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getChargingItemId() {
		return chargingItemId;
	}
	public void setChargingItemId(Long chargingItemId) {
		this.chargingItemId = chargingItemId;
	}
	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}
	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
    
}
