package com.everhomes.rest.techpark.rental;

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
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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




 
}
