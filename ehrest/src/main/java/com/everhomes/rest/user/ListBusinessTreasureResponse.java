package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;
/**
 * 查询电商用户的财富
 * @author elians
 *<ul>
 *<li>couponCount:礼券数</li>
 *<li>myCoupon:我的礼券</li>
 *<li>myOrderUrl:我的订单</li>
 *<li>applyShopUrl:申请开店</li>
 *<li>isAppliedShop:是否已申请过开店</li>
 *<li>orderCount:待发货订单数量</li>
 *<li>businessUrl:电商链接</li>
 *<li>businessRealm:电商realm</li>
 *</ul>
 */
public class ListBusinessTreasureResponse {
    private Integer couponCount;
    private String myCoupon;
    private String myOrderUrl;
    private String applyShopUrl;
    private Integer isAppliedShop;
    private Integer orderCount;
    private String businessUrl;
    private String businessRealm;
	

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public String getMyCoupon() {
		return myCoupon;
	}

	public void setMyCoupon(String myCoupon) {
		this.myCoupon = myCoupon;
	}

	public String getMyOrderUrl() {
		return myOrderUrl;
	}

	public void setMyOrderUrl(String myOrderUrl) {
		this.myOrderUrl = myOrderUrl;
	}

	public String getApplyShopUrl() {
		return applyShopUrl;
	}

	public void setApplyShopUrl(String applyShopUrl) {
		this.applyShopUrl = applyShopUrl;
	}

	public Integer getIsAppliedShop() {
		return isAppliedShop;
	}

	public void setIsAppliedShop(Integer isAppliedShop) {
		this.isAppliedShop = isAppliedShop;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public String getBusinessUrl() {
		return businessUrl;
	}

	public void setBusinessUrl(String businessUrl) {
		this.businessUrl = businessUrl;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getBusinessRealm() {
		return businessRealm;
	}

	public void setBusinessRealm(String businessRealm) {
		this.businessRealm = businessRealm;
	}

}
