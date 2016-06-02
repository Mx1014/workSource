package com.everhomes.rest.techpark.rental.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>siteName: 名称</li>
 * <li>spec: 规格</li>
 * <li>address: 地址</li>
 * <li>longitude: 地址经度</li>
 * <li>latitude: 地址纬度</li>
 * <li>contactPhonenum: 咨询电话</li>
 * <li>chargeName: 负责人</li>
 * <li>introduction: 详情</li>
 * <li>coverUri: 封面图uri</li>
 * <li>detailUris: 详情图</li>
 * <li>owners: 可见社区</li>
 * <li>status: 是否开启，1是0否</li>
 * </ul>
 */
public class GetResourceListAdminResponse {
	private String siteName;
	private String spec;
	private String address;
	private Double longitude;
	private Double latitude;
	private String contactPhonenum;
	private String chargeName;
	private String introduction;
	private String coverUri;
	@ItemType(String.class)
	private List<String> detailUris;
	@ItemType(SiteOwnerDTO.class)
	private List<SiteOwnerDTO> owners;
	private Byte status;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public String getContactPhonenum() {
		return contactPhonenum;
	}

	public void setContactPhonenum(String contactPhonenum) {
		this.contactPhonenum = contactPhonenum;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getCoverUri() {
		return coverUri;
	}

	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}

	public List<String> getDetailUris() {
		return detailUris;
	}

	public void setDetailUris(List<String> detailUris) {
		this.detailUris = detailUris;
	}

	public List<SiteOwnerDTO> getOwners() {
		return owners;
	}

	public void setOwners(List<SiteOwnerDTO> owners) {
		this.owners = owners;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}