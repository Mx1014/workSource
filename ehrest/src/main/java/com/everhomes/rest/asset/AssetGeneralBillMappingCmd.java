//@formatter:off
package com.everhomes.rest.asset;

/**
 *<ul>
 * <li>assetCategoryId:缴费多应用ID</li>
 * <li>namespaceId:域空间ID</li>
 * <li>ownerType:所属者type</li>
 * <li>ownerId:所属者id</li>
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>goodsServeType: 商品-服务类别</li>
 * <li>goodsNamespace: 商品-域空间</li>
 * <li>goodsTag1:商品-服务提供方标识1</li>
 * <li>goodsTag2:商品-服务提供方标识2</li>
 * <li>goodsTag3:商品-服务提供方标识3</li>
 * <li>goodsTag4:商品-服务提供方标识4</li>
 * <li>goodsTag5:商品-服务提供方标识5</li>
 *</ul>
 */
public class AssetGeneralBillMappingCmd {
	private Long assetCategoryId; 
	private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String sourceType;
    private Long sourceId;
    private String goodsServeType;
    private String goodsNamespace;
    private String goodsTag1;
    private String goodsTag2;
    private String goodsTag3;
    private String goodsTag4;
    private String goodsTag5;
    
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public String getGoodsServeType() {
		return goodsServeType;
	}
	public void setGoodsServeType(String goodsServeType) {
		this.goodsServeType = goodsServeType;
	}
	public String getGoodsNamespace() {
		return goodsNamespace;
	}
	public void setGoodsNamespace(String goodsNamespace) {
		this.goodsNamespace = goodsNamespace;
	}
	public String getGoodsTag1() {
		return goodsTag1;
	}
	public void setGoodsTag1(String goodsTag1) {
		this.goodsTag1 = goodsTag1;
	}
	public String getGoodsTag2() {
		return goodsTag2;
	}
	public void setGoodsTag2(String goodsTag2) {
		this.goodsTag2 = goodsTag2;
	}
	public String getGoodsTag3() {
		return goodsTag3;
	}
	public void setGoodsTag3(String goodsTag3) {
		this.goodsTag3 = goodsTag3;
	}
	public String getGoodsTag4() {
		return goodsTag4;
	}
	public void setGoodsTag4(String goodsTag4) {
		this.goodsTag4 = goodsTag4;
	}
	public String getGoodsTag5() {
		return goodsTag5;
	}
	public void setGoodsTag5(String goodsTag5) {
		this.goodsTag5 = goodsTag5;
	}
	public Long getAssetCategoryId() {
		return assetCategoryId;
	}
	public void setAssetCategoryId(Long assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}
}
