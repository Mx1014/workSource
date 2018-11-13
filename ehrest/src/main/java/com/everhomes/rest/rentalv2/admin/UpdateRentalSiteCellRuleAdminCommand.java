package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalPriceClassificationDTO;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>resourceType: resourceType</li>
 * <li>resourceId: 资源id</li>
 * <li>ruleId: 选取单元格id</li>
 * <li>beginTime: 开始时间对于按小时则是N或者N.5，对于半天则是0早上1下午2晚上</li>
 * <li>endTime: 结束时间对于按小时则是N或者N.5，对于半天则是0早上1下午2晚上</li>
 * <li>status: 状态，0启用 -1停用参考{@link com.everhomes.rest.rentalv2.RentalSiteStatus}</li>
 * <li>originalPrice: 原价-如果打折则有（园区客户）</li>
 * <li>price: 实际价格-打折则为折后价（园区客户）</li>
 * <li>initiatePrice: 园区客户起步后价格</li>
 * <li>counts: 可预约数量</li>
 * <li>loopType: 循环方式 参考{@link com.everhomes.rest.rentalv2.LoopType}</li>
 * <li>wholeDayFlag: 是否修改全天的单元格(按小时预约)</li>
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li>
 * <li>orgMemberOriginalPrice: 原价-如果打折则有(企业内部价)</li>
 * <li>orgMemberInitiatePrice: 集团内部起步后价格</li>
 * <li>orgMemberPrice: 实际价格-打折则为折后价(企业内部价)</li>
 * <li>approvingUserOriginalPrice: 原价-如果打折则有（外部客户价）</li>
 * <li>approvingUserInitiatePrice: 外部客户起步后价格</li>
 * <li>approvingUserPrice: 实际价格-打折则为折后价（外部客户价）</li>
 * <li>rentalType: rentalType {@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>sitePackages: 套餐价格表{@link com.everhomes.rest.rentalv2.admin.PricePackageDTO}</li>
 * <li>sitePackageId: 对应套餐id</li>
 * </ul>
 */
public class UpdateRentalSiteCellRuleAdminCommand {

	private String resourceType;
	private Long resourceId;
	private Long ruleId;
	//按小时或者半天
//	private Double beginTime;
//	private Double endTime;

	private Byte status;

	private java.math.BigDecimal originalPrice;
	private java.math.BigDecimal price;
	private BigDecimal initiatePrice;

	private java.lang.Double counts;
	private Byte loopType;
	private Byte wholeDayFlag;

	private Long beginDate;
	private Long endDate;

	private BigDecimal orgMemberOriginalPrice;
	private BigDecimal orgMemberInitiatePrice;
	private BigDecimal orgMemberPrice;

	private BigDecimal approvingUserOriginalPrice;
	private BigDecimal approvingUserInitiatePrice;
	private BigDecimal approvingUserPrice;

	private Byte rentalType;
	private Byte userPriceType;
	@ItemType(PricePackageDTO.class)
	private List<PricePackageDTO> sitePackages;
	private List<RentalPriceClassificationDTO> classifications;
	private Long sitePackageId;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public java.math.BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(java.math.BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public java.lang.Double getCounts() {
		return counts;
	}

	public void setCounts(java.lang.Double counts) {
		this.counts = counts;
	}

	public Byte getLoopType() {
		return loopType;
	}

	public void setLoopType(Byte loopType) {
		this.loopType = loopType;
	}

	public Long getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public List<PricePackageDTO> getSitePackages() {
		return sitePackages;
	}

	public void setSitePackages(List<PricePackageDTO> sitePackages) {
		this.sitePackages = sitePackages;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Long getSitePackageId() {
		return sitePackageId;
	}

	public void setSitePackageId(Long sitePackageId) {
		this.sitePackageId = sitePackageId;
	}

	public BigDecimal getOrgMemberOriginalPrice() {
		return orgMemberOriginalPrice;
	}

	public void setOrgMemberOriginalPrice(BigDecimal orgMemberOriginalPrice) {
		this.orgMemberOriginalPrice = orgMemberOriginalPrice;
	}

	public BigDecimal getOrgMemberPrice() {
		return orgMemberPrice;
	}

	public void setOrgMemberPrice(BigDecimal orgMemberPrice) {
		this.orgMemberPrice = orgMemberPrice;
	}

	public BigDecimal getApprovingUserOriginalPrice() {
		return approvingUserOriginalPrice;
	}

	public void setApprovingUserOriginalPrice(BigDecimal approvingUserOriginalPrice) {
		this.approvingUserOriginalPrice = approvingUserOriginalPrice;
	}

	public BigDecimal getApprovingUserPrice() {
		return approvingUserPrice;
	}

	public void setApprovingUserPrice(BigDecimal approvingUserPrice) {
		this.approvingUserPrice = approvingUserPrice;
	}

	public BigDecimal getInitiatePrice() {
		return initiatePrice;
	}

	public void setInitiatePrice(BigDecimal initiatePrice) {
		this.initiatePrice = initiatePrice;
	}

	public BigDecimal getOrgMemberInitiatePrice() {
		return orgMemberInitiatePrice;
	}

	public void setOrgMemberInitiatePrice(BigDecimal orgMemberInitiatePrice) {
		this.orgMemberInitiatePrice = orgMemberInitiatePrice;
	}

	public BigDecimal getApprovingUserInitiatePrice() {
		return approvingUserInitiatePrice;
	}

	public void setApprovingUserInitiatePrice(BigDecimal approvingUserInitiatePrice) {
		this.approvingUserInitiatePrice = approvingUserInitiatePrice;
	}

	public Byte getUserPriceType() {
		return userPriceType;
	}

	public void setUserPriceType(Byte userPriceType) {
		this.userPriceType = userPriceType;
	}

	public Byte getWholeDayFlag() {
		return wholeDayFlag;
	}

	public void setWholeDayFlag(Byte wholeDayFlag) {
		this.wholeDayFlag = wholeDayFlag;
	}

	public List<RentalPriceClassificationDTO> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<RentalPriceClassificationDTO> classifications) {
		this.classifications = classifications;
	}
}
