// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>startTime : 开始统计时间</li>
 * <li>endTime : 结束统计时间</li>
 * <li>orderStatus : 订单状态，全部则为空, 参考 {@link com.everhomes.rest.print.PrintOrderStatusType}</li>
 * <li>jobType : 任务类型，全部则为空, 参考 {@link com.everhomes.rest.print.PrintJobTypeType}</li>
 * <li>keywords: 关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * <li>payType:支付方式，全部则为空,参考{@link com.everhomes.rest.organization.VendorType}</li>
 * <li>payMode:支付类型，全部则为空，参考 {@link com.everhomes.rest.general.order.GorderPayType}</li>
 * <li>paymentType:账单的支付方式（0-线下缴费，1-微信支付，2-对公转账，8-支付宝支付）</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintRecordsCommand {
	private String ownerType;
	private Long ownerId;
	private Long startTime;
	private Long endTime;
	private Byte orderStatus;
	private Byte jobType;
	private String keywords;
	private Long pageAnchor;
	private Integer pageSize;
	private Integer namespaceId;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;
	private String payType;
	private Byte paymentType;
	private Byte payMode;
	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
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
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Byte getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Byte getJobType() {
		return jobType;
	}
	public void setJobType(Byte jobType) {
		this.jobType = jobType;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Byte getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Byte paymentType) {
		this.paymentType = paymentType;
	}

	
	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
