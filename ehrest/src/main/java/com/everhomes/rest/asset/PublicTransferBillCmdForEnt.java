//@formatter:off
package com.everhomes.rest.asset;
import java.util.List;
/**
 * @author created by yangcx
 * @date 2018年5月23日----上午11:03:53
 */

/**
 *<ul>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>targetType:客户类型,sceneType为default，family时，类型为eh_user即个人，当sceneType为pm_admin屏蔽，当sceneType为其他，则类型为eh_organization即企业</li>
 * <li>targetId:客户id，客户类型为企业时，targetId为企业id</li>
 * <li>payerName：缴费人</li>
 * <li>namespaceId:域空间</li>
 * <li>contractNum:合同编号</li>
 * <li>orderSource:订单来源，如：物业缴费</li>
 * <li>billList:勾选的要转账的账单集合，参考{@link PaymentBillRequest}</li>
 *</ul>
 */
public class PublicTransferBillCmdForEnt {
	private String ownerType;
	private Long ownerId;
	private String targetType;
	private Long targetId;
	private String payerName;
	private Integer namespaceId;
	private String contractNum;
	private String orderSource;
	private List<PaymentBillRequest> billList;
	
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
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public List<PaymentBillRequest> getBillList() {
		return billList;
	}
	public void setBillList(List<PaymentBillRequest> billList) {
		this.billList = billList;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	
}
