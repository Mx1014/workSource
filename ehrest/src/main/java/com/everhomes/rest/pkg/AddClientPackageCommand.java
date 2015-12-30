// @formatter:off
package com.everhomes.rest.pkg;

import com.everhomes.util.StringHelper;

public class AddClientPackageCommand {
	private String name;
	
	private long versionCode;
	
	/** 1: user edition, 2: business edition, 3: community edition */
	private byte packageEdition;
	
	/** 1: andriod, 2: ios */
	private byte devicePlatform;
	
	/** 1: official site */
	private int distributionChannel;
	
	private String tag;
	
	private String jsonParams;
	
	public AddClientPackageCommand() {
		
	}
	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(long versionCode) {
		this.versionCode = versionCode;
	}

	public byte getPackageEdition() {
		return packageEdition;
	}

	public void setPackageEdition(byte packageEdition) {
		this.packageEdition = packageEdition;
	}

	public byte getDevicePlatform() {
		return devicePlatform;
	}

	public void setDevicePlatform(byte devicePlatform) {
		this.devicePlatform = devicePlatform;
	}

	public int getDistributionChannel() {
		return distributionChannel;
	}

	public void setDistributionChannel(int distributionChannel) {
		this.distributionChannel = distributionChannel;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getJsonParams() {
		return jsonParams;
	}

	public void setJsonParams(String jsonParams) {
		this.jsonParams = jsonParams;
	}

	public String toString() {
        return StringHelper.toJsonString(this);
    }
}
