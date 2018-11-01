package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId : namespaceId</li>
 * <li>merchantId : 商户id</li>
 * <li>serviceType : 业务名称 如打印/停车缴费，业务不关注，透传回去即可</li>
 * <li>bizType : 服务类型,当前为模块id 如云打印是41400</li>
 * <li>tagKeys : tag各层级标识 如项目id，停车场id</li>
 * 注：对不同的模块，每层的tag意义是不同的，如云打印tag2表示的是打印机的名称
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月29日
 */
public class GetGoodListCommand {
	private Integer namespaceId;
	private Long merchantId;
	private Long bizType;
	GoodTagInfo goodTagInfo;
 	
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getBizType() {
		return bizType;
	}

	public void setBizType(Long bizType) {
		this.bizType = bizType;
	}

	public GoodTagInfo getGoodTagInfo() {
		return goodTagInfo;
	}

	public void setGoodTagInfo(GoodTagInfo goodTagInfo) {
		this.goodTagInfo = goodTagInfo;
	}
}
