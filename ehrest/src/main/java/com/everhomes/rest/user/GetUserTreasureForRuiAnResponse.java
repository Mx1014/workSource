package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>point: 积分数量 </li>
 *     <li>coupon: 卡包数量 </li>
 *     <li>order: 订单数量 </li>
 *     <li>vipLevelText: 会员等级</li>
 *     <li>task: 任务数量</li>
 *     <li>pointUrl: 积分跳转URL</li>
 *     <li>couponUrl: 卡包跳转URL</li>
 *     <li>orderUrl: 订单跳转url</li>
 *     <li>vipUrl: 会员跳转url</li>
 *     <li>taskUrl: 任务跳转url，预留字段，现暂时没有跳转url</li>
 * </ul>
 */
public class GetUserTreasureForRuiAnResponse {

    private Long point;
    private Long coupon;
    private Long order;
    private String vipLevelText;
    private Integer task;

    private String pointUrl;
    private String couponUrl;
    private String orderUrl;
    private String vipUrl;
    private String taskUrl;

    public String getVipLevelText() {
        return vipLevelText;
    }

    public void setVipLevelText(String vipLevelText) {
        this.vipLevelText = vipLevelText;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public String getVipUrl() {
        return vipUrl;
    }

    public void setVipUrl(String vipUrl) {
        this.vipUrl = vipUrl;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getPointUrl() {
        return pointUrl;
    }

    public void setPointUrl(String pointUrl) {
        this.pointUrl = pointUrl;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Long getCoupon() {
        return coupon;
    }

    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
