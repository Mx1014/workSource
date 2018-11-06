package com.everhomes.rest.goods;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespacceId :域空间</li>
 * <li>bizType : 服务类型,当前为模块id 如云打印是41400</li>
 * <li>apiType : 所选类型：ALL,CATEGORY,GOODS </li>
 * <li>categoryId ：商品类型ID </li>
 * </ul>
 * @author miaozhou 
 * @date 2018年10月7日
 */
public class GetServiceGoodCommand {
	
	private Long bizType;
	private String apiType;
	private String categoryId;
	private Integer namespaceId;
	


	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	

	public Long getBizType() {
		return bizType;
	}

	public void setBizType(Long bizType) {
		this.bizType = bizType;
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
