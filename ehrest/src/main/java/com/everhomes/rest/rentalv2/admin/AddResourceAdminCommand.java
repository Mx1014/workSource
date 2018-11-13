package com.everhomes.rest.rentalv2.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalSiteFileDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区) 
 * <li>resourceTypeId: 图标id</li>
 * <li>organizationId: 所属公司id</li>
 * <li>siteName: 名称</li>
 * <li>spec: 规格</li>
 * <li>address: 地址</li>
 * <li>longitude: 地址经度</li>
 * <li>latitude: 地址纬度</li>
 * <li>communityId: 所属园区Id</li>
 * <li>contactPhonenum: 咨询电话</li>
 * <li>chargeUid: 负责人id</li>
 * <li>introduction: 详情</li>
 * <li>notice: 备注信息</li>
 * <li>coverUri: 封面图uri</li>
 * <li>detailUris: 详情图</li>
 * <li>fileUris: 附件</li>
 * <li>owners: 可见社区</li>
 * <li>status: 是否开启，1是0否</li>
 * <li>confirmationPrompt: 确认提示(非必填)</li>
 * <li>offlineCashierAddress: 线下支付收银地址</li>
 * <li>offlinePayeeUid: 线下支付收款人id</li>
 * <li>aclinkId: 门禁组id</li>
 * <li>multiUnit: 是否允许预约多个场所</li>
 * <li>	autoAssign：       	是否动态分配 1是 0否	</li>
 * <li>	siteCounts：资源数量</li>
 * <li>	siteNumbers：资源编号列表 {String}</li>
 * </ul>
 */
public class AddResourceAdminCommand {

	private String resourceType;
	@NotNull
	private Long resourceTypeId;
	@NotNull
	private Long organizationId;
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
    private String offlinePayeeUid;
    private String aclinkId;
	private Byte multiUnit;
	private java.lang.Byte       autoAssign;
	private Double siteCounts;
	@ItemType(SiteNumberDTO.class)
	private List<SiteNumberDTO> siteNumbers;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

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

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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

	public String getOfflinePayeeUid() {
		return offlinePayeeUid;
	}

	public void setOfflinePayeeUid(String offlinePayeeUid) {
		this.offlinePayeeUid = offlinePayeeUid;
	}

	public String getAclinkId() {
		return aclinkId;
	}

	public void setAclinkId(String aclinkId) {
		this.aclinkId = aclinkId;
	}

	public Byte getMultiUnit() {
		return multiUnit;
	}

	public void setMultiUnit(Byte multiUnit) {
		this.multiUnit = multiUnit;
	}

	public Byte getAutoAssign() {
		return autoAssign;
	}

	public void setAutoAssign(Byte autoAssign) {
		this.autoAssign = autoAssign;
	}

	public Double getSiteCounts() {
		return siteCounts;
	}

	public void setSiteCounts(Double siteCounts) {
		this.siteCounts = siteCounts;
	}

	public List<SiteNumberDTO> getSiteNumbers() {
		return siteNumbers;
	}

	public void setSiteNumbers(List<SiteNumberDTO> siteNumbers) {
		this.siteNumbers = siteNumbers;
	}

	public List<RentalSiteFileDTO> getFileUris() {
		return fileUris;
	}

	public void setFileUris(List<RentalSiteFileDTO> fileUris) {
		this.fileUris = fileUris;
	}
}