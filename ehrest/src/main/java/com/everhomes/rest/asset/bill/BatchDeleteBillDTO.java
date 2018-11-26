//@formatter:off
package com.everhomes.rest.asset.bill;

/**
 *<ul>
 * <li>billId: 账单ID</li>
 * <li>dateStrBegin: 账单开始时间</li>
 * <li>dateStrEnd: 账单结束时间</li>
 * <li>billGroupName: 账单组名称</li>
 * <li>deleteErrorMsg: 删除失败信息</li>
 *</ul>
 */
public class BatchDeleteBillDTO {
	private Long billId;
	private String dateStrBegin;
	private String dateStrEnd;
	private String billGroupName;
	private String deleteErrorMsg;
	
	public String getDateStrBegin() {
		return dateStrBegin;
	}
	public void setDateStrBegin(String dateStrBegin) {
		this.dateStrBegin = dateStrBegin;
	}
	public String getDateStrEnd() {
		return dateStrEnd;
	}
	public void setDateStrEnd(String dateStrEnd) {
		this.dateStrEnd = dateStrEnd;
	}
	public String getBillGroupName() {
		return billGroupName;
	}
	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}
	public String getDeleteErrorMsg() {
		return deleteErrorMsg;
	}
	public void setDeleteErrorMsg(String deleteErrorMsg) {
		this.deleteErrorMsg = deleteErrorMsg;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
}
