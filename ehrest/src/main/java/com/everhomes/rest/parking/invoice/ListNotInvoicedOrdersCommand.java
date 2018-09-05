// @formatter:off
package com.everhomes.rest.parking.invoice;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>userId : (Long)用户id</li>
  *<li>businessType :  (String)业务类型（parking=停车缴费）</li>
  *<li>pageSize : (Integer)页面大小</li>
  *<li>pageAnchor : (Long)下一页锚点</li>
  *<li>startCreateTime : (Long)创建开始时间</li>
  *<li>endCreateTime : (Long)创建结束时间</li>
  *</ul>
  */

public class ListNotInvoicedOrdersCommand {
    private Long userId;
    private String businessType="parking";
    private Integer pageSize;
    private Long pageAnchor;
    private Long startCreateTime;
    private Long endCreateTime;
    private Long orderNo;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Long getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(Long startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public Long getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(Long endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public Long getOrderNo() { return orderNo; }

    public void setOrderNo(Long orderNo) { this.orderNo = orderNo; }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
