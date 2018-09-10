package com.everhomes.rest.bundleid_mapper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>identify: appstore、 develop和appbeta 三种 (必填)</li>
 * <li>bundleId: 关联应用(必填)</li>
 * </ul>
 * @author huanglm 20180607
 *
 */
public class BundleidMapperCommand {

	/**
	 * 域空间ID
	 */
	@NotNull
    private Integer namespaceId;
	
    /**
     * pusherIdentify中截取的标志字符串
     */
	@NotNull
    private String identify;
	
    /**
     * 关联应用
     */
	@NotNull
    private String bundleId;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getIdentify() {
		return identify;
	}
	public void setIdentify(String identify) {
		this.identify = identify;
	}
	public String getBundleId() {
		return bundleId;
	}
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
     
}

