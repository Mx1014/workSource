// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: id，更新时有，创建时无</li>
 * <li>category: 类型，1寄件人地址，2收件人地址，参考{@link com.everhomes.rest.express.ExpressAddressCategory}</li>
 * <li>userName: 姓名</li>
 * <li>phone: 手机号</li>
 * <li>organizationId: 组织id</li>
 * <li>organizationName: 组织名称</li>
 * <li>province: 省</li>
 * <li>city: 市</li>
 * <li>county: 区县</li>
 * <li>detailAddress: 详细地址</li>
 * <li>defaultFlag: 是否默认，1是0否，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ExpressAddressDTO {
	private Long id;

	private Byte category;

	private String userName;

	private String phone;

	private Long organizationId;

	private String organizationName;

	private Long provinceId;

	private Long cityId;

	private Long countyId;
	
	private String province;

	private String city;

	private String county;

	private String detailAddress;

	private Byte defaultFlag;

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getCategory() {
		return category;
	}

	public void setCategory(Byte category) {
		this.category = category;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public Byte getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(Byte defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
