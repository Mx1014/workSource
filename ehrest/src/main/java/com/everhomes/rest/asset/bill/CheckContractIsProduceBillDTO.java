//@formatter:off
package com.everhomes.rest.asset.bill;

/**
 *<ul>
 * <li>contractId: 合同ID</li>
 * <li>paymentStatus: 合同是否产生已缴账单，参考{com.everhomes.rest.asset.AssetPaymentBillStatus，UNPAID((byte)0, "待缴"), PAID((byte)1, "已缴")}</li>
 *</ul>
 */
public class CheckContractIsProduceBillDTO {
	private Long contractId;
	private Byte paymentStatus;
	
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	public Byte getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(Byte paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
}
