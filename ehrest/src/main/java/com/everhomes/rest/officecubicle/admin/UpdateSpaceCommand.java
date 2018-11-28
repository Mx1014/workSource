package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeAttachmentDTO;
import com.everhomes.rest.officecubicle.OfficeCategoryDTO;
import com.everhomes.rest.officecubicle.OfficeRangeDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * add工位预定的空间 
 * <li>namespaceId :namespaceId;</li>
 * <li>ownerType : community 工位发布的范围</li>
 * <li>ownerId : communityId 范围的id</li>
 *<li> id: id</li>
 *<li> name: 工位空间名称	</li>
*<li> provinceId : 省份id	</li>
*<li> provinceName : 省份名称	</li>
*<li>cityId  : 城市id	</li>
*<li> cityName : 城市名称	</li>
*<li> address : 地址	</li>
*<li> longitude : 经度	</li>
*<li>latitude  : 纬度	</li>
*<li>contactPhone  : 咨询电话	</li>
*<li> chargeUid : 负责人uid	</li>
*<li> • description : 详情-html片	</li>
*<li> spaceCoverUri : 封面图片uri</li>
 * <li>spaceAttachments: 空间banner图的urls{@link com.everhomes.rest.officecubicle.OfficeAttachmentDTO} </li> 
 * <li> stationCoverUri : 开放式工位图片uri</li>
 * <li> shortRentUri : 短租封面图片uri</li>
 * <li>shortRentAttachments: 短租banner图的urls{@link com.everhomes.rest.officecubicle.OfficeAttachmentDTO} </li> 
 * <li>categories: 工位空间list{@link com.everhomes.rest.officecubicle.OfficeCategoryDTO}</li> 
 * <li>openFlag:1开启，0关闭</li>
 * <li>ranges: 空间可见范围{@link OfficeRangeDTO}</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * <li>longRentPrice:长租工位价格</li>
 * <li>shortRentNums:短租工位数量</li>
 * </ul>
 */
public class UpdateSpaceCommand { 
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
	private String description;
	@ItemType(OfficeAttachmentDTO.class)
	private List<OfficeAttachmentDTO> spaceAttachments;
	@ItemType(OfficeAttachmentDTO.class)
	private List<OfficeAttachmentDTO> shortRentAttachments;
	@ItemType(OfficeAttachmentDTO.class)
	private List<OfficeAttachmentDTO> stationAttachments; 
	@ItemType(OfficeCategoryDTO.class)
	private List<OfficeCategoryDTO> categories;
	@ItemType(OfficeRangeDTO.class)
	private List<OfficeRangeDTO> ranges;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;
	private Byte openFlag;
	private BigDecimal longRentPrice;
	private String shortRentNums;
	
	
	public String getShortRentNums() {
		return shortRentNums;
	}

	public void setShortRentNums(String shortRentNums) {
		this.shortRentNums = shortRentNums;
	}

	public BigDecimal getLongRentPrice() {
		return longRentPrice;
	}

	public void setLongRentPrice(BigDecimal longRentPrice) {
		this.longRentPrice = longRentPrice;
	}

	public Byte getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(Byte openFlag) {
		this.openFlag = openFlag;
	}

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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getManagerUid() {
		return managerUid;
	}


	public void setManagerUid(Long managerUid) {
		this.managerUid = managerUid;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}



	public List<OfficeAttachmentDTO> getSpaceAttachments() {
		return spaceAttachments;
	}

	public void setSpaceAttachments(List<OfficeAttachmentDTO> spaceAttachments) {
		this.spaceAttachments = spaceAttachments;
	}


	public List<OfficeAttachmentDTO> getShortRentAttachments() {
		return shortRentAttachments;
	}

	public void setShortRentAttachments(List<OfficeAttachmentDTO> shortRentAttachments) {
		this.shortRentAttachments = shortRentAttachments;
	}



	public List<OfficeAttachmentDTO> getStationAttachments() {
		return stationAttachments;
	}

	public void setStationAttachments(List<OfficeAttachmentDTO> stationAttachments) {
		this.stationAttachments = stationAttachments;
	}

	public List<OfficeCategoryDTO> getCategories() {
		return categories;
	}


	public void setCategories(List<OfficeCategoryDTO> categories) {
		this.categories = categories;
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
  

 
}
