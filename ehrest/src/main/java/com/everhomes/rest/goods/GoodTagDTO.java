package com.everhomes.rest.goods;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serviceType : 根据请求参数回填</li>
 * <li>namespaceId : namespaceId</li>
 * <li>tag1 : tag1</li>
 * <li>tag2 : tag2</li>
 * <li>tag3 : tag3</li>
 * <li>tag4 : tag4</li>
 * <li>tag5 : tag5</li>
 * <li>goodsTag : 商品标识</li>
 * <li>goodsName : 商品名称</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月9日
 */
public class GoodTagDTO {
	private String serviceType;
	private Integer namespaceId;
	private String tag1;
	private String tag2;
	private String tag3;
	private String tag4;
	private String tag5;
	private String goodsTag;
	private String goodsName;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}

	public String getTag2() {
		return tag2;
	}

	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}

	public String getTag3() {
		return tag3;
	}

	public void setTag3(String tag3) {
		this.tag3 = tag3;
	}

	public String getTag4() {
		return tag4;
	}

	public void setTag4(String tag4) {
		this.tag4 = tag4;
	}

	public String getTag5() {
		return tag5;
	}

	public void setTag5(String tag5) {
		this.tag5 = tag5;
	}

	public String getGoodsTag() {
		return goodsTag;
	}

	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

}
