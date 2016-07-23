package com.everhomes.rest.officecubicle;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定的空间
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
*<li> chargeUid : 负责人uid	</li>
*<li> chargeName : 负责人name</li>
*<li> chargePhone : 负责人telephone	</li>
*<li> details : 详情-html片	</li>
*<li> coverUri : 封面图片uri</li>
*<li> coverUrl : 封面图片url</li> 
 * <li>bannerUrls: banner图的urls </li> 
 * <li>sites: 工位空间list{@link com.everhomes.rest.officecubicle.OfficeSiteDTO}</li> 
 * </ul>
 */
public class OfficeSpaceDTO {
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
	private Long chargeUid;
	private String chargeName;
	private String chargePhone;
	private String details;
	private String coverUri;
	private String coverUrl;
	@ItemType(String.class)
	private List<String> bannerUrls;
	@ItemType(OfficeSiteDTO.class)
	private List<OfficeSiteDTO> sites;

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

	public Long getChargeUid() {
		return chargeUid;
	}

	public void setChargeUid(Long chargeUid) {
		this.chargeUid = chargeUid;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getChargePhone() {
		return chargePhone;
	}

	public void setChargePhone(String chargePhone) {
		this.chargePhone = chargePhone;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
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
 

	public List<String> getBannerUrls() {
		return bannerUrls;
	}

	public void setBannerUrls(List<String> bannerUrls) {
		this.bannerUrls = bannerUrls;
	}

	public List<OfficeSiteDTO> getSites() {
		return sites;
	}

	public void setSites(List<OfficeSiteDTO> sites) {
		this.sites = sites;
	}
	
}
