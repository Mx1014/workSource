//@formatter:off
package com.everhomes.rest.asset.bill;

/**
 *<ul>
 * <li>contractId: 合同ID</li>
 * <li>paymentStatus: 合同是否产生已缴账单，参考{com.everhomes.rest.asset.AssetPaymentBillStatus，UNPAID((byte)0, "待缴"), PAID((byte)1, "已缴")}</li>
 * <li>deleteSuccess: 是否删除成功，参考{com.everhomes.rest.asset.bill.DeleteContractBillFlag，FAIL((byte)0, "删除失败"), SUCCESS((byte)1, "删除成功")</li>
 *</ul>
 */
public class BatchDeleteBillFromContractDTO {
	private Long contractId;
	private Byte paymentStatus;
	private Byte deleteSuccess;
	
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
	public Byte getDeleteSuccess() {
		return deleteSuccess;
	}
	public void setDeleteSuccess(Byte deleteSuccess) {
		this.deleteSuccess = deleteSuccess;
	}
	
}
