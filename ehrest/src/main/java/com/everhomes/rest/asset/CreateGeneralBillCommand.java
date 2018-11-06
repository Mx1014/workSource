//@formatter:off
package com.everhomes.rest.asset;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.rest.promotion.order.GoodDTO;

/**
 *<ul>
 * <li>namespaceId:域空间ID</li>
 * <li>ownerType:所属者type</li>
 * <li>ownerId:所属者id</li>
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>sourceName:账单来源（如：停车缴费）</li>
 * <li>consumeUserId:记账人ID</li>
 * <li>consumeUserName:记账人名称</li>
 * <li>merchantOrderId:统一订单定义的唯一标识</li>
 * <li>targetType:客户类别,参考{@link com.everhomes.rest.asset.AssetTargetType}</li>
 * <li>targetId:客户id</li>
 * <li>targetName:客户名称</li>
 * <li>amountReceivable:应收金额（含税）（原始总金额，不计算优惠的金额）</li>
 * <li>exemptionAmount:优惠/减免金额</li>
 * <li>exemptionRemark:优惠/减免的备注说明</li>
 * <li>isSettled:是否是已出账单,1:新增已出账单;0:新增未出账单（如何不传该参数，接口会自动根据账单组的设置去判断是已出或未出）</li>
 * <li>invoiceNum:发票单号</li>
 * <li>dateStrBegin:计费开始（不传默认当前时间）</li>
 * <li>dateStrEnd:计费结束（不传，默认按照当前时间+账单组的设置去计算得出）</li>
 * <li>goodDTOList:商品信息列表,参考{@link com.everhomes.rest.promotion.order.GoodDTO}</li>
 *</ul>
 */
public class CreateGeneralBillCommand {
	private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String sourceType;
    private Long sourceId;
    private String sourceName;
    private Long consumeUserId;
    private String consumeUserName;
    private String merchantOrderId;
    private String targetType;
    private Long targetId;
    private String targetName;
    private BigDecimal amountReceivable;
    private BigDecimal exemptionAmount;
    private String exemptionRemark;
    private String invoiceNum;
    private Byte isSettled;
    private String dateStrBegin;
    private String dateStrEnd;
    private List<GoodDTO> goodDTOList;
    
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
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public Long getConsumeUserId() {
		return consumeUserId;
	}
	public void setConsumeUserId(Long consumeUserId) {
		this.consumeUserId = consumeUserId;
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
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}
	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public Byte getIsSettled() {
		return isSettled;
	}
	public void setIsSettled(Byte isSettled) {
		this.isSettled = isSettled;
	}
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
	public BigDecimal getExemptionAmount() {
		return exemptionAmount;
	}
	public void setExemptionAmount(BigDecimal exemptionAmount) {
		this.exemptionAmount = exemptionAmount;
	}
	public String getExemptionRemark() {
		return exemptionRemark;
	}
	public void setExemptionRemark(String exemptionRemark) {
		this.exemptionRemark = exemptionRemark;
	}
	public List<GoodDTO> getGoodDTOList() {
		return goodDTOList;
	}
	public void setGoodDTOList(List<GoodDTO> goodDTOList) {
		this.goodDTOList = goodDTOList;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public String getConsumeUserName() {
		return consumeUserName;
	}
	public void setConsumeUserName(String consumeUserName) {
		this.consumeUserName = consumeUserName;
	}
    
}
