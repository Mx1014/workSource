package com.everhomes.rest.goods;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serviceType : 根据请求参数回填</li>
 * <li>serviceTypeName : 根据请求参数回填</li>
 * <li>namespaceId : namespaceId</li>
 * <li>tag1Key : tag1Key</li>
 * <li>tag2Key : tag2Key</li>
 * <li>tag3Key : tag3Key</li>
 * <li>tag4Key : tag4Key</li>
 * <li>tag5Key : tag5Key</li>
 * <li>tag1Name : tag1Name</li>
 * <li>tag2Name : tag2Name</li>
 * <li>tag3Name : tag3Name</li>
 * <li>tag4Name : tag4Name</li>
 * <li>tag5Name : tag5Name</li>
 * <li>goodsTag : 商品标识</li>
 * <li>goodsName : 商品名称</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月9日
 */
public class GoodTagInfo {
	private Long serviceType;
	private String serviceTypeName;
	private Integer namespaceId;
	private String tag1Key;
	private String tag2Key;
	private String tag3Key;
	private String tag4Key;
	private String tag5Key;
	private String tag1Name;
	private String tag2Name;
	private String tag3Name;
	private String tag4Name;
	private String tag5Name;
	private String goodsTag;
	private String goodsName;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public String getTag1Key() {
		return tag1Key;
	}

	public void setTag1Key(String tag1Key) {
		this.tag1Key = tag1Key;
	}

	public String getTag2Key() {
		return tag2Key;
	}

	public void setTag2Key(String tag2Key) {
		this.tag2Key = tag2Key;
	}

	public String getTag3Key() {
		return tag3Key;
	}

	public void setTag3Key(String tag3Key) {
		this.tag3Key = tag3Key;
	}

	public String getTag4Key() {
		return tag4Key;
	}

	public void setTag4Key(String tag4Key) {
		this.tag4Key = tag4Key;
	}

	public String getTag5Key() {
		return tag5Key;
	}

	public void setTag5Key(String tag5Key) {
		this.tag5Key = tag5Key;
	}

	public String getTag1Name() {
		return tag1Name;
	}

	public void setTag1Name(String tag1Name) {
		this.tag1Name = tag1Name;
	}

	public String getTag2Name() {
		return tag2Name;
	}

	public void setTag2Name(String tag2Name) {
		this.tag2Name = tag2Name;
	}

	public String getTag3Name() {
		return tag3Name;
	}

	public void setTag3Name(String tag3Name) {
		this.tag3Name = tag3Name;
	}

	public String getTag4Name() {
		return tag4Name;
	}

	public void setTag4Name(String tag4Name) {
		this.tag4Name = tag4Name;
	}

	public String getTag5Name() {
		return tag5Name;
	}

	public void setTag5Name(String tag5Name) {
		this.tag5Name = tag5Name;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getServiceType() {
		return serviceType;
	}

	public void setServiceType(Long serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

}
