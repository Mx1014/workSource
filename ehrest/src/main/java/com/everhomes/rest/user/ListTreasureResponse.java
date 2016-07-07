package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;
/**
 * 查询用户的财富
 * @author elians
 *<ul>
 *<li>points:积分数</li>
 *<li>couponCount:礼券数</li>
 *<li>topicFavoriteCount:收藏的帖子数</li>
 *<li>sharedCount:发帖数</li>
 *<li>pointRuleUrl:积分规则url</li>
 *<li>level:用户等级</li>
 *<li>myCoupon:我的礼券</li>
 *<li>myOrderUrl:我的订单</li>
 *<li>applyShopUrl:申请开店</li>
 *<li>isAppliedShop:是否已申请过开店</li>
 *<li>orderCount:待发货订单数量</li>
 *</ul>
 */
public class ListTreasureResponse {
    private Integer points;
    private Integer couponCount;
    private Integer topicFavoriteCount;
    private Integer sharedCount;
    private String pointRuleUrl;
    private Byte level;
    private String myCoupon;
    private String myOrderUrl;
    private String applyShopUrl;
    private Integer isAppliedShop;
    private Integer orderCount;
	
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


    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(Integer couponCount) {
        this.couponCount = couponCount;
    }

    public Integer getTopicFavoriteCount() {
        return topicFavoriteCount;
    }

    public void setTopicFavoriteCount(Integer topicFavoriteCount) {
        this.topicFavoriteCount = topicFavoriteCount;
    }

    public Integer getSharedCount() {
        return sharedCount;
    }

    public void setSharedCount(Integer sharedCount) {
        this.sharedCount = sharedCount;
    }

    public String getPointRuleUrl() {
        return pointRuleUrl;
    }

    public void setPointRuleUrl(String pointRuleUrl) {
        this.pointRuleUrl = pointRuleUrl;
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public String getMyCoupon() {
        return myCoupon;
    }

    public void setMyCoupon(String myCoupon) {
        this.myCoupon = myCoupon;
    }

    public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
