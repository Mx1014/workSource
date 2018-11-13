package com.everhomes.rest.rentalv2.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalSiteFileDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 资源id</li>
 * <li>siteName: 名称</li>
 * <li>spec: 规格</li>
 * <li>address: 地址</li>
 * <li>longitude: 地址经度</li>
 * <li>latitude: 地址纬度</li>
 * <li>communityId: 所属园区Id</li>
 * <li>contactPhonenum: 咨询电话</li>
 * <li>chargeUid: 负责人id</li>
 * <li>introduction: 详情</li>
 * <li>notice: notice</li>
 * <li>coverUri: 封面图uri</li>
 * <li>detailUris: 详情图</li>
 * <li>fileUris: 附件</li>
 * <li>owners: 可见社区 {@link com.everhomes.rest.rentalv2.admin.SiteOwnerDTO}</li>
 * <li>status: 是否开启，1是0否</li>
 * <li>confirmationPrompt: 确认提示(非必填)</li>
 * <li>offlineCashierAddress: 线下支付收银地址</li>
 * <li>offlinePayeeUid: 线下支付收款人id</li>
 * <li>aclinkId: aclinkId</li>
 * </ul>
 */
public class UpdateResourceAdminCommand {

	private String resourceType;
	private Long id;
	@NotNull
	private String siteName;
	@NotNull
	private String spec;
	@NotNull
	private String address;
	@NotNull
	private Double longitude;
	@NotNull
	private Double latitude;
	private Long communityId;
	private String contactPhonenum;
	@NotNull
	private String chargeUid;
	@NotNull
	private String introduction;
	private String notice;
	@NotNull
	private String coverUri;
	@ItemType(String.class)
	private List<String> detailUris;
	private List<RentalSiteFileDTO> fileUris;
	@ItemType(SiteOwnerDTO.class)
	private List<SiteOwnerDTO> owners;
	private Byte status;
	private String confirmationPrompt;
	private String offlineCashierAddress;
	private Long offlinePayeeUid;
	private String aclinkId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


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

	public String getChargeUid() {
		return chargeUid;
	}

	public void setChargeUid(String chargeUid) {
		this.chargeUid = chargeUid;
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


	public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	public String getNotice() {
		return notice;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}


	public String getConfirmationPrompt() {
		return confirmationPrompt;
	}


	public void setConfirmationPrompt(String confirmationPrompt) {
		this.confirmationPrompt = confirmationPrompt;
	}


	public String getOfflineCashierAddress() {
		return offlineCashierAddress;
	}


	public void setOfflineCashierAddress(String offlineCashierAddress) {
		this.offlineCashierAddress = offlineCashierAddress;
	}


	public Long getOfflinePayeeUid() {
		return offlinePayeeUid;
	}


	public void setOfflinePayeeUid(Long offlinePayeeUid) {
		this.offlinePayeeUid = offlinePayeeUid;
	}

	public String getAclinkId() {
		return aclinkId;
	}

	public void setAclinkId(String aclinkId) {
		this.aclinkId = aclinkId;
	}

	public List<RentalSiteFileDTO> getFileUris() {
		return fileUris;
	}

	public void setFileUris(List<RentalSiteFileDTO> fileUris) {
		this.fileUris = fileUris;
	}
}