package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定的空间
  * <li>namespaceId : namespaceId</li>
 * <li>ownerType : community 工位发布的范围</li>
 * <li>ownerId : communityId 范围的id</li>
 *<li> id: id	</li>
 *<li> name: 工位空间名称	</li>
*<li> provinceId : 省份id	</li>
*<li> provinceName : 省份名称	</li>
*<li>cityId  : 城市id	</li>
*<li> cityName : 城市名称	</li>
*<li> address : 地址	</li>
*<li> longitude : 经度	</li>
*<li>latitude  : 纬度	</li>
*<li>contactPhone  : 咨询电话	</li>
*<li> managerUid : 负责人uid	</li>
*<li> managerName : 负责人name</li>
*<li> managerPhone : 负责人telephone	</li>
*<li> description : 详情-html片	</li>
*<li> coverUri : 封面图片uri</li>
*<li> coverUrl : 封面图片url</li> 
*<li> status : space 状态 2-普通 0-删除</li> 
 * <li>attachments: banner图的urls{@link com.everhomes.rest.officecubicle.OfficeAttachmentDTO} </li> 
 * <li>categories: 工位空间list{@link com.everhomes.rest.officecubicle.OfficeCategoryDTO}</li> 
 * <li>ranges: 空间可见范围{@link OfficeRangeDTO}</li>
 * <li>minUnitPrice: 最低价格</li>
 * <li>allPositionNums: 总工位数量</li>
 * <li>refundTip:退款提示</li>
 * <li>holidayOpenFlag:节假日是否开放预约 0不开放 1 开放</li>
 * <li>holidayeType:节假日类型 {@link com.everhomes.rest.rentalv2.admin.RentalHolidayType}</li>
 * </ul>
 */
public class OfficeSpaceDTO {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long id;
	private String name;
	private Long provinceId;
	private String provinceName;
	private Long cityId;
	private String cityName;
	private String address;
	private double longitude;
	private double latitude;
	private String contactPhone;
	private Long managerUid;
	private String managerName;
	private String managerPhone;
	private String description;
	private String coverUri;
	private String coverUrl;
	@ItemType(OfficeAttachmentDTO.class)
	private List<OfficeAttachmentDTO> attachments;
	@ItemType(OfficeCategoryDTO.class)
	private List<OfficeCategoryDTO> categories;
	private Byte status;
	private BigDecimal minUnitPrice;
	private Integer allPositionNums;
	private String refundTip;
	private Byte holidayOpenFlag;
	private Byte holidayType;
	private BigDecimal settingPrice;

	
	public BigDecimal getSettingPrice() {
		return settingPrice;
	}

	public void setSettingPrice(BigDecimal settingPrice) {
		this.settingPrice = settingPrice;
	}

	public String getRefundTip() {
		return refundTip;
	}

	public void setRefundTip(String refundTip) {
		this.refundTip = refundTip;
	}

	public Byte getHolidayOpenFlag() {
		return holidayOpenFlag;
	}

	public void setHolidayOpenFlag(Byte holidayOpenFlag) {
		this.holidayOpenFlag = holidayOpenFlag;
	}

	public Byte getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(Byte holidayType) {
		this.holidayType = holidayType;
	}

	public BigDecimal getMinUnitPrice() {
		return minUnitPrice;
	}

	public void setMinUnitPrice(BigDecimal minUnitPrice) {
		this.minUnitPrice = minUnitPrice;
	}

	public Integer getAllPositionNums() {
		return allPositionNums;
	}

	public void setAllPositionNums(Integer allPositionNums) {
		this.allPositionNums = allPositionNums;
	}

	@ItemType(OfficeRangeDTO.class)
	private List<OfficeRangeDTO> ranges;
	
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

	public List<OfficeRangeDTO> getRanges() {
		return ranges;
	}

	public void setRanges(List<OfficeRangeDTO> ranges) {
		this.ranges = ranges;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public Long getManagerUid() {
		return managerUid;
	}

	public void setManagerUid(Long managerUid) {
		this.managerUid = managerUid;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerPhone() {
		return managerPhone;
	}

	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoverUri() {
		return coverUri;
	}

	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public List<OfficeAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<OfficeAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public List<OfficeCategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<OfficeCategoryDTO> categories) {
		this.categories = categories;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
 
	
}
