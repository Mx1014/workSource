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
 *<li>businessUrl:电商链接</li>
 *<li>businessRealm:电商realm</li>
 *<li>activityDefaultListStyle:活动列表默认样式</li>
 *<li>shakeOpenDoorUser:当前用户是否开启摇一摇</li>
 *<li>shakeOpenDoorNamespace:当前域空间是否开启</li>
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
    private String businessUrl;
    private String businessRealm;
    private Byte activityDefaultListStyle;
    private Byte shakeOpenDoorUser;
    private Byte shakeOpenDoorNamespace;
	
	public Byte getActivityDefaultListStyle() {
		return activityDefaultListStyle;
	}

	public void setActivityDefaultListStyle(Byte activityDefaultListStyle) {
		this.activityDefaultListStyle = activityDefaultListStyle;
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

	public Byte getShakeOpenDoorUser() {
		return shakeOpenDoorUser;
	}

	public void setShakeOpenDoorUser(Byte shakeOpenDoorUser) {
		this.shakeOpenDoorUser = shakeOpenDoorUser;
	}

	public Byte getShakeOpenDoorNamespace() {
		return shakeOpenDoorNamespace;
	}

	public void setShakeOpenDoorNamespace(Byte shakeOpenDoorNamespace) {
		this.shakeOpenDoorNamespace = shakeOpenDoorNamespace;
	}

}
