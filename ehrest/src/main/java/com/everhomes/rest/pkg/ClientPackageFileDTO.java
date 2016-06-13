package com.everhomes.rest.pkg;

import com.everhomes.util.StringHelper;

public class ClientPackageFileDTO {
	private java.lang.Long     id;
	private java.lang.String   name;
	private java.lang.Long     versionCode;
	private java.lang.Byte     packageEdition;
	private java.lang.Byte     devicePlatform;
	private java.lang.Integer  distributionChannel;
	private java.lang.String   tag;
	private java.lang.String   jsonParams;
	
	public java.lang.Long getId() {
		return id;
	}
	
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	
	public java.lang.String getName() {
		return name;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.Long getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(java.lang.Long versionCode) {
		this.versionCode = versionCode;
	}
	
	public java.lang.Byte getPackageEdition() {
		return packageEdition;
	}
	
	public void setPackageEdition(java.lang.Byte packageEdition) {
		this.packageEdition = packageEdition;
	}
	
	public java.lang.Byte getDevicePlatform() {
		return devicePlatform;
	}
	
	public void setDevicePlatform(java.lang.Byte devicePlatform) {
		this.devicePlatform = devicePlatform;
	}
	
	public java.lang.Integer getDistributionChannel() {
		return distributionChannel;
	}
	
	public void setDistributionChannel(java.lang.Integer distributionChannel) {
		this.distributionChannel = distributionChannel;
	}
	
	public java.lang.String getTag() {
		return tag;
	}
	
	public void setTag(java.lang.String tag) {
		this.tag = tag;
	}
	
	public java.lang.String getJsonParams() {
		return jsonParams;
	}
	
	public void setJsonParams(java.lang.String jsonParams) {
		this.jsonParams = jsonParams;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
