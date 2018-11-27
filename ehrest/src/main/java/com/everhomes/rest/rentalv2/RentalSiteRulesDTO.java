package com.everhomes.rest.rentalv2;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>id：id</li>
 * <li>rentalSiteId：场所id</li> 
 * <li>rentalType： time(0),halfday(1){@link com.everhomes.rest.rentalv2.RentalType} </li>
 * <li>priceType: 0 按时长定价 1 起步价模式</li>
 * <li>amorpm： am(0),pm(1){@link com.everhomes.rest.rentalv2.AmorpmFlag} </li> 
 * <li>beginTime：开始时间(MM:SS)</li>
 * <li>endTime：结束时间(MM:SS)</li>
 * <li>counts：场所剩余数量</li>
 * <li>resourceCounts：场所数量</li>
 * <li>unit：场所单位:1or0.5</li> 
 * <li>rentalStep：最小预定时间(整数，rentalType=0为多少个半小时，rentalType=1为多少个半天，rentalType=2为多少天)</li>
 * <li>TimeStep：坐标轴单位时间（小时）</li> 
 * <li>price：场所价格</li>
 * <li>initiatePrice: 起步后价格</li>
 * <li>	originalPrice：     	原价（如果不为null则price为打折价）	</li>
 * <li>halfsitePrice：半场价格</li> 
 * <li>	halfsiteOriginalPrice：     	半场原价（如果不为null则price为打折价）	</li>
 * <li>	exclusiveFlag：     	是否为独占资源0否 1 是	</li>
 * <li>	autoAssign：        	是否动态分配 1是 0否	</li>
 * <li>	multiUnit：         	是否允许预约多个场所 1是 0否	</li>
 * <li>	multiTimeInterval： 	是否允许预约多个时段 1是 0否	</li>
 * <li>ruleDate：规则日期</li>  
 * <li>Status： OPEN(0),CLOSE(1){@link com.everhomes.rest.rentalv2.SiteRuleStatus} </li> 
 * <li>siteNumber：场所编号：订单详情中用</li>
 * <li>orgMemberOriginalPrice: 原价-如果打折则有(企业内部价)</li>
 * <li>orgMemberPrice: 实际价格-打折则为折后价(企业内部价)</li>
 * <li>orgMemberInitiatePrice: 集团内部起步后价格</li>
 * <li>approvingUserOriginalPrice: 原价-如果打折则有（外部客户价）</li>
 * <li>approvingUserPrice: 实际价格-打折则为折后价（外部客户价）</li>
 * <li>approvingUserInitiatePrice: 外部客户起步后价格</li>
 * </ul>
 */
public class RentalSiteRulesDTO {
	private Long id;
	private Long rentalSiteId; 
	private Byte rentalType;
	private Byte priceType;
	private Byte amorpm;
	private Long beginTime;
	private Long endTime;
	private Double counts;
	private Double resourceCounts;
	private Double unit;
	private java.math.BigDecimal price;
	private BigDecimal initiatePrice;
	private java.math.BigDecimal originalPrice;
	private java.math.BigDecimal halfsitePrice;
	private java.math.BigDecimal halfsiteOriginalPrice;
	private java.lang.Byte       exclusiveFlag;
	private java.lang.Byte       autoAssign;
	private java.lang.Byte       multiUnit;
	private java.lang.Byte       multiTimeInterval;
	private Integer rentalStep;
	private Double timeStep;
	private Long ruleDate; 
	private Byte status;
	private String siteNumber;

	private BigDecimal orgMemberOriginalPrice;
	private BigDecimal orgMemberPrice;
	private BigDecimal orgMemberInitiatePrice;

	private BigDecimal approvingUserOriginalPrice;
	private BigDecimal approvingUserPrice;
	private BigDecimal approvingUserInitiatePrice;
	private BigDecimal halfOrgMemberOriginalPrice;
	private BigDecimal halfOrgMemberPrice;
	private BigDecimal halfApprovingUserOriginalPrice;
	private BigDecimal halfApprovingUserPrice;
	@ItemType(RentalSitePackagesDTO.class)
	private List<RentalSitePackagesDTO> sitePackages;
	//新版本的价格
	private List<RentalPriceClassificationDTO> priceRules;
	private Byte userPriceType;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public BigDecimal getHalfOrgMemberOriginalPrice() {
		return halfOrgMemberOriginalPrice;
	}

	public void setHalfOrgMemberOriginalPrice(BigDecimal halfOrgMemberOriginalPrice) {
		this.halfOrgMemberOriginalPrice = halfOrgMemberOriginalPrice;
	}

	public BigDecimal getHalfOrgMemberPrice() {
		return halfOrgMemberPrice;
	}

	public void setHalfOrgMemberPrice(BigDecimal halfOrgMemberPrice) {
		this.halfOrgMemberPrice = halfOrgMemberPrice;
	}

	public BigDecimal getHalfApprovingUserOriginalPrice() {
		return halfApprovingUserOriginalPrice;
	}

	public void setHalfApprovingUserOriginalPrice(BigDecimal halfApprovingUserOriginalPrice) {
		this.halfApprovingUserOriginalPrice = halfApprovingUserOriginalPrice;
	}

	public BigDecimal getHalfApprovingUserPrice() {
		return halfApprovingUserPrice;
	}

	public void setHalfApprovingUserPrice(BigDecimal halfApprovingUserPrice) {
		this.halfApprovingUserPrice = halfApprovingUserPrice;
	}

	public Double getResourceCounts() {
		return resourceCounts;
	}

	public void setResourceCounts(Double resourceCounts) {
		this.resourceCounts = resourceCounts;
	}

	public Long getRentalSiteId() {
		return rentalSiteId;
	}

	public List<RentalSitePackagesDTO> getSitePackages() {
		return sitePackages;
	}

	public void setSitePackages(List<RentalSitePackagesDTO> sitePackages) {
		this.sitePackages = sitePackages;
	}

	public java.math.BigDecimal getOriginalPrice() {
		return originalPrice;
	}



	public void setOriginalPrice(java.math.BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}



	public java.lang.Byte getExclusiveFlag() {
		return exclusiveFlag;
	}



	public void setExclusiveFlag(java.lang.Byte exclusiveFlag) {
		this.exclusiveFlag = exclusiveFlag;
	}



	public java.lang.Byte getAutoAssign() {
		return autoAssign;
	}



	public void setAutoAssign(java.lang.Byte autoAssign) {
		this.autoAssign = autoAssign;
	}



	public java.lang.Byte getMultiUnit() {
		return multiUnit;
	}



	public void setMultiUnit(java.lang.Byte multiUnit) {
		this.multiUnit = multiUnit;
	}



	public java.lang.Byte getMultiTimeInterval() {
		return multiTimeInterval;
	}



	public void setMultiTimeInterval(java.lang.Byte multiTimeInterval) {
		this.multiTimeInterval = multiTimeInterval;
	}



	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}
 
	public Double getUnit() {
		return unit;
	}



	public void setUnit(Double unit) {
		this.unit = unit;
	}
 
 


 


	public Byte getStatus() {
		return status;
	}



	public void setStatus(Byte status) {
		this.status = status;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Long getBeginTime() {
		return beginTime;
	}



	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}



	public Long getEndTime() {
		return endTime;
	}



	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}



	public Double getCounts() {
		return counts;
	}



	public void setCounts(Double counts) {
		this.counts = counts;
	}



	public Long getRuleDate() {
		return ruleDate;
	}



	public void setRuleDate(Long ruleDate) {
		this.ruleDate = ruleDate;
	}


 


	public java.math.BigDecimal getPrice() {
		return price;
	}



	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}



	public Byte getRentalType() {
		return rentalType;
	}



	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}



	public Byte getAmorpm() {
		return amorpm;
	}



	public void setAmorpm(Byte amorpm) {
		this.amorpm = amorpm;
	}



	public Integer getRentalStep() {
		return rentalStep;
	}



	public void setRentalStep(Integer rentalStep) {
		this.rentalStep = rentalStep;
	}



	public Double getTimeStep() {
		return timeStep;
	}



	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}



	public String getSiteNumber() {
		return siteNumber;
	}



	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}



	public java.math.BigDecimal getHalfsitePrice() {
		return halfsitePrice;
	}



	public void setHalfsitePrice(java.math.BigDecimal halfsitePrice) {
		this.halfsitePrice = halfsitePrice;
	}



	public java.math.BigDecimal getHalfsiteOriginalPrice() {
		return halfsiteOriginalPrice;
	}



	public void setHalfsiteOriginalPrice(java.math.BigDecimal halfsiteOriginalPrice) {
		this.halfsiteOriginalPrice = halfsiteOriginalPrice;
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

	public Byte getPriceType() {
		return priceType;
	}

	public void setPriceType(Byte priceType) {
		this.priceType = priceType;
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

	public BigDecimal getApprovingUserPrice() {
		return approvingUserPrice;
	}

	public void setApprovingUserPrice(BigDecimal approvingUserPrice) {
		this.approvingUserPrice = approvingUserPrice;
	}

	public Byte getUserPriceType() {
		return userPriceType;
	}

	public void setUserPriceType(Byte userPriceType) {
		this.userPriceType = userPriceType;
	}

	public List<RentalPriceClassificationDTO> getPriceRules() {
		return priceRules;
	}

	public void setPriceRules(List<RentalPriceClassificationDTO> priceRules) {
		this.priceRules = priceRules;
	}
}
