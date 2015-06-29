package com.everhomes.user;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
