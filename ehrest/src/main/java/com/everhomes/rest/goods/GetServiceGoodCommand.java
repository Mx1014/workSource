package com.everhomes.rest.goods;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespacceId :域空间</li>
 * <li>serviceType : 服务类别 0-全部,1-资产,2-停车,3-云打印, 4-资源预约, 5-活动, 6-电商'</li>
 * <li>apiType : 所选类型：ALL,CATEGORY,GOODS </li>
 * <li>categoryId ：商品类型ID </li>
 * </ul>
 * @author miaozhou 
 * @date 2018年10月7日
 */
public class GetServiceGoodCommand {
	
	private Byte serviceType;
	private String apiType;
	private String categoryId;
	private Integer namespaceId;
	


	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	
	public Byte getServiceType() {
		return serviceType;
	}

	public void setServiceType(Byte serviceType) {
		this.serviceType = serviceType;
	}

	public String getApiType() {
		return apiType;
	}
	
	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
}
