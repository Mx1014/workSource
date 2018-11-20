// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>resourceTypeId: 资源id</li>
 * <li>pageType: 样式{@link com.everhomes.rest.rentalv2.RentalPageType} </li>
 * <li>payMode: 支付模式 0 :线上支付 1 :线下支付 2 :审批线上支付/li>
 * <li>identify: 资源类型的分类 {@link com.everhomes.rest.rentalv2.RentalV2ResourceType}</li>
 * <li>unauthVisible: 非认证用户是否可见，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>invoiceEntryFlag: 发票入口开关</li>
 * <li>limitCommunityFlag:是否仅本项目可见 </li>
 * <li>crossCommuFlag:是否支持跨项目 </li>
 * </ul>
 */
public class RentalInstanceConfig {

	private Long resourceTypeId;

	private Byte pageType;

	private Byte payMode;

	private String identify;

	private Byte unauthVisible;

	private Byte invoiceEntryFlag;

    private Byte limitCommunityFlag;

    private Byte crossCommuFlag;

    public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Byte getPageType() {
		return pageType;
	}

	public void setPageType(Byte pageType) {
		this.pageType = pageType;
	}

	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public Byte getUnauthVisible() {
		return unauthVisible;
	}

	public void setUnauthVisible(Byte unauthVisible) {
		this.unauthVisible = unauthVisible;
	}

	public Byte getInvoiceEntryFlag() {
		return invoiceEntryFlag;
	}

	public void setInvoiceEntryFlag(Byte invoiceEntryFlag) {
		this.invoiceEntryFlag = invoiceEntryFlag;
	}

	public Byte getLimitCommunityFlag() {
		return limitCommunityFlag;
	}

	public void setLimitCommunityFlag(Byte limitCommunityFlag) {
		this.limitCommunityFlag = limitCommunityFlag;
	}

	public Byte getCrossCommuFlag() {
		return crossCommuFlag;
	}

	public void setCrossCommuFlag(Byte crossCommuFlag) {
		this.crossCommuFlag = crossCommuFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
