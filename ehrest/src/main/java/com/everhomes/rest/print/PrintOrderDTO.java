// @formatter:off
package com.everhomes.rest.print;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id : 订单id</li>
 * <li>jobType : 订单状态，全部则为空, 参考 {@link com.everhomes.rest.print.PrintJobTypeType}</li>
 * <li>creatorUid : 发起人id</li>
 * <li>createTime : 发起时间</li>
 * <li>orderTotalAmount : 金额</li>
 * <li>orderStatus : 订单状态，全部则为空, 参考 {@link com.everhomes.rest.print.PrintOrderStatusType}</li>
 * <li>printDocumentName : 打印文档名称</li>
 * <li>detail : 详情</li>
 * <li>orderNo : 订单编号</li>
 * <li>email : 发件邮件</li>
 * <li>payMode : 支付类型</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class PrintOrderDTO {
	private Long id;
	private Byte jobType;
	private Long creatorUid;
	private Timestamp createTime;
	private BigDecimal orderTotalAmount;
	private Byte orderStatus;
	private String printDocumentName;
	private String detail;
	private Long orderNo;
	private String email;
	private String payMode;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getJobType() {
		return jobType;
	}
	public void setJobType(Byte jobType) {
		this.jobType = jobType;
	}
	public Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getOrderTotalAmount() {
		return orderTotalAmount;
	}
	public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}
	public Byte getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPrintDocumentName() {
		return printDocumentName;
	}
	public void setPrintDocumentName(String printDocumentName) {
		this.printDocumentName = printDocumentName;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
