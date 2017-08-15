package com.everhomes.rest.rentalv2;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>id：id</li>
 * <li>rentalSiteId：场所id</li>
 * <li>itemName：商品名称</li>
 * <li>itemPrice：商品价格</li>
 * <li>counts：商品数量</li> 
 * <li>imgUri：商品图片uri</li> 
 * <li>defaultOrder：商品排序</li> 
 * <li>itemType：商品类型 参考{@link com.everhomes.rest.rentalv2.RentalItemType}}</li> 
 * </ul>
 */
public class UpdateItemAdminCommand {
	@NotNull
	private Long id;
	@NotNull
	private Long rentalSiteId;
	@NotNull
	private String itemName;
	@NotNull
	private java.math.BigDecimal itemPrice;
	@NotNull
	private Integer counts;
	private java.lang.String     imgUri;
	private java.lang.Integer    defaultOrder;
	private java.lang.Byte       itemType;
	private String description;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getRentalSiteId() {
		return rentalSiteId;
	}
	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	} 
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	} 
	
	public java.lang.String getImgUri() {
		return imgUri;
	}
	public void setImgUri(java.lang.String imgUri) {
		this.imgUri = imgUri;
	}
	public java.lang.Integer getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(java.lang.Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	public java.math.BigDecimal getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(java.math.BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public java.lang.Byte getItemType() {
		return itemType;
	}
	public void setItemType(java.lang.Byte itemType) {
		this.itemType = itemType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
