package com.everhomes.rest.appurl;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>osType: 操作系统类型 参考{@link com.everhomes.rest.user.OSType}</li>
 *  <li>downloadUrl: 应用商店链接</li>
 *  <li>id: 主键</li>
 * </ul>
 *
 */
public class AppUrlDeviceDTO {
	
	private Long id ;
	private Byte osType;
	private String downloadUrl;
	

	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Byte getOsType() {
		return osType;
	}



	public void setOsType(Byte osType) {
		this.osType = osType;
	}



	public String getDownloadUrl() {
		return downloadUrl;
	}



	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
