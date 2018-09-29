package com.everhomes.rest.goods;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId : namespaceId</li>
 * <li>communityId : 项目id</li>
 * <li>appOriginId : 应用originId </li>
 * <li>merchantId : 商户id</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月29日
 */
public class GetGoodListCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private Long appOriginId;
	private Long merchantId;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getAppOriginId() {
		return appOriginId;
	}

	public void setAppOriginId(Long appOriginId) {
		this.appOriginId = appOriginId;
	}
}
