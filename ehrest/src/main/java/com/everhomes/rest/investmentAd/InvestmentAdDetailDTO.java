package com.everhomes.rest.investmentAd;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 招商广告id</li>
 * 	<li>title: 招商广告标题</li>
 * 	<li>advertisementType: 广告类型（1-招租广告，2-招商广告）</li>
 * 	<li>businessStatus: 招商状态</li>
 * 	<li>availableAreaMin: 最小招商面积</li>
 * 	<li>availableAreaMax: 最大招商面积</li>
 * 	<li>assetPriceMin: 最小租金</li>
 * 	<li>assetPriceMax: 最大租金</li>
 * 	<li>priceUnit: 租金单位</li>
 * 	<li>apartmentFloorMin: 最小楼层</li>
 * 	<li>apartmentFloorMax: 最大楼层</li>
 *  <li>orientation: 朝向</li>
 *  <li>longitude: 经度</li>
 *  <li>latitude: 纬度</li>
 *  <li>contacts: 联系人</li>
 *  <li>contactPhone: 联系电话</li>
 *  <li>description: 招商广告描述</li>
 *  <li>postUri: 招商广告封面图</li>
 *  <li>defaultOrder: 排序字段</li>
 *  <li>createTime: 招商广告创建时间</li>
 *  <li>assetDispalyFlag: 招商广告是否显示关联楼宇房源</li>
 *  <li>relatedAssets: 招商广告关联的楼宇房源，参考{@link com.everhomes.rest.investmentAd.RelatedAssetDTO}</li>
 *  <li>attachments: 招商广告轮播图，参考{@link com.everhomes.rest.investmentAd.InvestmentAdBannerDTO}</li>
 *  <li>customFormFlag: 是否启用了自定义表单</li>
 *  <li>generalFormId: 自定义表单id</li>
 *  <li>formValues: 自定义表单传值，参考{@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 */
public class InvestmentAdDetailDTO {
	
	private Long id;
	private Integer namespaceId;
	private Long communityId;
	private String ownerType;
	private Long ownerId;
	private String title;
	private Byte investmentType;
	private Byte investmentStatus;
	private BigDecimal availableAreaMin;
	private BigDecimal availableAreaMax;
	private BigDecimal assetPriceMin;
	private BigDecimal assetPriceMax;
	private Byte priceUnit;
	private Integer apartmentFloorMin;
	private Integer apartmentFloorMax;
	private String orientation;
	private String address;
	private Double longitude;
	private Double latitude;
	private String contactName;
	private String contactPhone;
	private String description;
	private String posterUri;
	private String posterUrl;
	private Long defaultOrder;
	private Timestamp createTime;
	
	private Byte assetDispalyFlag;
	@ItemType(RelatedAssetDTO.class)
	private List<RelatedAssetDTO> relatedAssets;
	
	@ItemType(InvestmentAdBannerDTO.class)
	private List<InvestmentAdBannerDTO> banners;
	
	private Byte customFormFlag;
	private Long generalFormId;
	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> formValues;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Byte getInvestmentType() {
		return investmentType;
	}

	public void setInvestmentType(Byte investmentType) {
		this.investmentType = investmentType;
	}

	public Byte getInvestmentStatus() {
		return investmentStatus;
	}

	public void setInvestmentStatus(Byte investmentStatus) {
		this.investmentStatus = investmentStatus;
	}

	public BigDecimal getAvailableAreaMin() {
		return availableAreaMin;
	}

	public void setAvailableAreaMin(BigDecimal availableAreaMin) {
		this.availableAreaMin = availableAreaMin;
	}

	public BigDecimal getAvailableAreaMax() {
		return availableAreaMax;
	}

	public void setAvailableAreaMax(BigDecimal availableAreaMax) {
		this.availableAreaMax = availableAreaMax;
	}

	public BigDecimal getAssetPriceMin() {
		return assetPriceMin;
	}

	public void setAssetPriceMin(BigDecimal assetPriceMin) {
		this.assetPriceMin = assetPriceMin;
	}

	public BigDecimal getAssetPriceMax() {
		return assetPriceMax;
	}

	public void setAssetPriceMax(BigDecimal assetPriceMax) {
		this.assetPriceMax = assetPriceMax;
	}

	public Byte getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(Byte priceUnit) {
		this.priceUnit = priceUnit;
	}

	public Integer getApartmentFloorMin() {
		return apartmentFloorMin;
	}

	public void setApartmentFloorMin(Integer apartmentFloorMin) {
		this.apartmentFloorMin = apartmentFloorMin;
	}

	public Integer getApartmentFloorMax() {
		return apartmentFloorMax;
	}

	public void setApartmentFloorMax(Integer apartmentFloorMax) {
		this.apartmentFloorMax = apartmentFloorMax;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosterUri() {
		return posterUri;
	}

	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}
	
	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
	
	public Long getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Long defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Byte getAssetDispalyFlag() {
		return assetDispalyFlag;
	}

	public void setAssetDispalyFlag(Byte assetDispalyFlag) {
		this.assetDispalyFlag = assetDispalyFlag;
	}

	public List<RelatedAssetDTO> getRelatedAssets() {
		return relatedAssets;
	}

	public void setRelatedAssets(List<RelatedAssetDTO> relatedAssets) {
		this.relatedAssets = relatedAssets;
	}

	public List<InvestmentAdBannerDTO> getBanners() {
		return banners;
	}

	public void setBanners(List<InvestmentAdBannerDTO> banners) {
		this.banners = banners;
	}

	public Byte getCustomFormFlag() {
		return customFormFlag;
	}

	public void setCustomFormFlag(Byte customFormFlag) {
		this.customFormFlag = customFormFlag;
	}

	public Long getGeneralFormId() {
		return generalFormId;
	}

	public void setGeneralFormId(Long generalFormId) {
		this.generalFormId = generalFormId;
	}

	public List<PostApprovalFormItem> getFormValues() {
		return formValues;
	}

	public void setFormValues(List<PostApprovalFormItem> formValues) {
		this.formValues = formValues;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
