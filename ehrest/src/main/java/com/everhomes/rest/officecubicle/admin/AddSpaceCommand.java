package com.everhomes.rest.officecubicle.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.officecubicle.OfficeAttachmentDTO;
import com.everhomes.rest.officecubicle.OfficeCategoryDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * add工位预定的空间 
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
*<li> description : 详情-html片	</li>
*<li> coverUri : 封面图片uri</li>
 * <li>attachments: banner图的urls{@link com.everhomes.rest.officecubicle.OfficeAttachmentDTO} </li> 
 * <li>categories: 工位空间list{@link com.everhomes.rest.officecubicle.OfficeCategoryDTO}</li> 
 * </ul>
 */
public class AddSpaceCommand { 
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
	private String coverUri; 
	@ItemType(OfficeAttachmentDTO.class)
	private List<OfficeAttachmentDTO> attachments;
	@ItemType(OfficeCategoryDTO.class)
	private List<OfficeCategoryDTO> categories;
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
	public String getCoverUri() {
		return coverUri;
	}
	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
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
  

 
}
