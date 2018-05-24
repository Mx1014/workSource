//@formatter:off
package com.everhomes.rest.asset;
/**
 * @author created by yangcx
 * @date 2018年5月23日----上午11:03:53
 */

import java.util.List;

/**
 *<ul>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 显示数量</li>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>targetType:对公转账客户类型为eh_organization即企业</li>
 * <li>targetId:客户id，对公转账只针对企业，targetId为企业id</li>
 * <li>namespaceId:域空间</li>
 * <li>startPayTime: 缴费开始时间</li>
 * <li>endPayTime: 缴费结束时间</li>
 * <li>payTime: 缴费时间</li>
 * <li>paymentOrderNum: 订单编号</li>
 * <li>orderType:账单类型，如：wuyeCode</li>
 * <li>userType:用户类型，如：EhOrganizations</li>
 * <li>userId:用户ID</li>
 * <li>transactionType:交易类型，如：手续费/充值/提现/退款等</li>
 * <li>transactionTypes:交易类型集合，如：手续费/充值/提现/退款等</li>
 *</ul>
 */
public class ListPaymentBillCmdForEnt {
	private Long pageAnchor;
	private Long pageSize;
	private String ownerType;
	private Long ownerId;
	private String targetType;
	private Long targetId;
	private Integer namespaceId;
	private String startPayTime;
    private String endPayTime;
    private String payTime;
    private String paymentOrderNum;
    private String orderType;
    private String userType;
    private Long userId;
    private Integer transactionType;
    private List<Integer> transactionTypes;
    private Boolean distributionRemarkIsNull;
    
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
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
	public String getStartPayTime() {
		return startPayTime;
	}
	public void setStartPayTime(String startPayTime) {
		this.startPayTime = startPayTime;
	}
	public String getEndPayTime() {
		return endPayTime;
	}
	public void setEndPayTime(String endPayTime) {
		this.endPayTime = endPayTime;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPaymentOrderNum() {
		return paymentOrderNum;
	}
	public void setPaymentOrderNum(String paymentOrderNum) {
		this.paymentOrderNum = paymentOrderNum;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	public List<Integer> getTransactionTypes() {
		return transactionTypes;
	}
	public void setTransactionTypes(List<Integer> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}
	public Boolean getDistributionRemarkIsNull() {
		return distributionRemarkIsNull;
	}
	public void setDistributionRemarkIsNull(Boolean distributionRemarkIsNull) {
		this.distributionRemarkIsNull = distributionRemarkIsNull;
	}
}
