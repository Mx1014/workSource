package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;
/**
 * <ul>
*<li>id：商品id</li>
 * <li>itemName：商品名称</li>
 * <li>itemPrice：商品价格</li>
 * <li>counts：商品数量</li> 
 * <li>imgUrl：商品图片</li> 
 * <li>defaultOrder：商品排序</li> 
 * <li>itemType：商品类型 参考{@link com.everhomes.rest.rentalv2.RentalItemType}}</li> 
 * <li>soldCount:已经卖出的商品数量-针对售卖型</li>
 * </ul>
 */
public class SiteItemDTO {
	private Long id;
	private String itemName;
	private BigDecimal itemPrice;
	private Integer counts;
	private java.lang.String     imgUri;
	private java.lang.String     imgUrl;
	private java.lang.Integer    defaultOrder;

	private java.lang.Byte       itemType;
	private Integer soldCount;
	private String description;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCounts() {
		return counts;
	}



	public void setCounts(Integer counts) {
		this.counts = counts;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



 




	public String getItemName() {
		return itemName;
	}







	public void setItemName(String itemName) {
		this.itemName = itemName;
	}







	public BigDecimal getItemPrice() {
		return itemPrice;
	}







	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}







	public java.lang.String getImgUri() {
		return imgUri;
	}







	public void setImgUri(java.lang.String imgUri) {
		this.imgUri = imgUri;
	}







	public java.lang.String getImgUrl() {
		return imgUrl;
	}







	public void setImgUrl(java.lang.String imgUrl) {
		this.imgUrl = imgUrl;
	}







	public java.lang.Integer getDefaultOrder() {
		return defaultOrder;
	}







	public void setDefaultOrder(java.lang.Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}







	public java.lang.Byte getItemType() {
		return itemType;
	}







	public void setItemType(java.lang.Byte itemType) {
		this.itemType = itemType;
	}







	public Integer getSoldCount() {
		return soldCount;
	}







	public void setSoldCount(Integer soldCount) {
		this.soldCount = soldCount;
	}




 
}
